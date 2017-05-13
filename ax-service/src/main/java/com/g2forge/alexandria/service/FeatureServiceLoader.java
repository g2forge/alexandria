package com.g2forge.alexandria.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Loads services based not only on their implementation of a basic service interface, but also based on the set of (not SPI visible) features they support.
 * 
 * As an example take a library for reading images from files. The SPI might have a method <code>Image load(Path path)</code> which loads some standard internal representation
 * from an on-disk file. While getting all the implementations of this SPI will help, a consumer might want to decide between which of the two it uses based on the set of
 * files they support.
 * 
 * This is implemented by have "feature" interfaces (interfaces without methods, but which extend the service) for each file format, and then allowing the various services to
 * extend those feature interfaces. This loader will ensure that while multiple services may be returned, it will never return one which implements a subset of a features of
 * any other service being returned by the same method.
 * 
 * In our example, a call to {@link #load()} might return two image loaders, one which handles JPEG and PNG, and another which handles BMP and JPEG. But it would not return a
 * third loader which handles just BMP, because the second loader can do that and more.
 * 
 * A second feature interface <code>B</code>, might extend a feature interface <code>A</code> to indicate that support for <code>B</code> includes all the features of
 * <code>A</code> and more.
 * 
 * In order for this loader to make proper choices about which services have the most features it must be able to determine which interfaces are feature interfaces and which
 * are not. Because the set of features may be extended by any new service implementation, this is discoverd through the use of {#link
 * {@link IServiceFeatureHierarchy#getFeatureInterfaces()}. Anyone defining a new feature interface should implement a class which implements {@link IServiceFeatureHierarchy},
 * and ensure that each new feature interface is returned from. Note that {@link IServiceFeatureHierarchy} may be implemented by a separate class (see
 * {@link #FeatureServiceLoader(Class, Class)}) or directly on the service itself.
 *
 * @param <S>
 */
public class FeatureServiceLoader<S> implements IServiceLoader<S> {
	protected class Features {
		protected final List<Class<? extends S>> features;

		public Features() {
			final Set<Class<? extends S>> raw = new HashSet<>();
			raw.add(getType());

			final Consumer<IServiceFeatureHierarchy<S>> adder = h -> h.getFeatureInterfaces().stream().filter(c -> getType().isAssignableFrom(c)).forEach(raw::add);
			getBasic().load().filter(s -> s instanceof IServiceFeatureHierarchy).map(h -> {
				@SuppressWarnings("unchecked")
				final IServiceFeatureHierarchy<S> cast = (IServiceFeatureHierarchy<S>) h;
				return cast;
			}).forEach(adder);

			final IServiceLoader<? extends IServiceFeatureHierarchy<S>> hierarchy = getHierarchy();
			if (hierarchy != null) hierarchy.load().forEach(adder);

			final LinkedList<Class<? extends S>> list = new LinkedList<>(raw);
			final Set<Class<? extends S>> features = new HashSet<>();
			features.add(getType());
			while (!list.isEmpty()) {
				final Class<? extends S> type = list.remove();
				if (!features.contains(type)) {
					features.add(type);
					list.addAll(Stream.of(type.getInterfaces()).flatMap(i -> {
						if (!getType().isAssignableFrom(i)) return Stream.empty();
						@SuppressWarnings("unchecked")
						final Class<? extends S> cast = (Class<? extends S>) i;
						return Stream.of(cast);
					}).collect(Collectors.toList()));
				}
			}

			this.features = new ArrayList<>(features);
		}

		public Set<Class<? extends S>> computeFeatures(S instance) {
			return features.stream().filter(f -> f.isInstance(instance)).collect(Collectors.toSet());
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final IServiceLoader<S> basic;

	@Getter(AccessLevel.PROTECTED)
	protected final IServiceLoader<? extends IServiceFeatureHierarchy<S>> hierarchy;

	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private final Features features = new Features();

	public FeatureServiceLoader(Class<S> type) {
		this(type, null);
	}

	public FeatureServiceLoader(Class<S> type, Class<? extends IServiceFeatureHierarchy<S>> hierarchy) {
		this.basic = new BasicServiceLoader<>(type);
		this.hierarchy = hierarchy == null ? null : new BasicServiceLoader<>(hierarchy);
	}

	@Override
	public Class<S> getType() {
		return getBasic().getType();
	}

	public Stream<? extends S> load() {
		return best(getBasic().load());
	}

	public <_S extends S> Stream<? extends S> load(Class<_S> subtype) {
		return best(getBasic().load(subtype));
	}

	protected Stream<? extends S> best(final Stream<? extends S> stream) {
		final List<S> list = stream.collect(Collectors.toList());
		if (list.size() < 2) return list.stream();

		final Map<S, Set<Class<? extends S>>> map = new IdentityHashMap<>();
		for (S service : list) {
			final Set<Class<? extends S>> features = getFeatures().computeFeatures(service);
			boolean shadowed = false;
			final Map<S, Object> remove = new IdentityHashMap<>();
			for (Map.Entry<S, Set<Class<? extends S>>> entry : map.entrySet()) {
				if (entry.getValue().containsAll(features)) {
					shadowed = true;
					break;
				} else if (features.containsAll(entry.getValue())) remove.put(entry.getKey(), service);
			}
			if (!shadowed) {
				remove.keySet().forEach(map::remove);
				map.put(service, features);
			}
		}
		return map.keySet().stream();
	}
}
