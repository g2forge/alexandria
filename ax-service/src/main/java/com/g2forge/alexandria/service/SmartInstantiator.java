package com.g2forge.alexandria.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_I;
import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class SmartInstantiator<S> extends DefaultInstantiator<S> {
	public static interface IInstanceBuilder {
		public <X> IInstanceBuilder arg(Class<X> type, X value);
	}

	@RequiredArgsConstructor
	@ToString
	public static class InstanceBuilder<T> implements ISupplier<T>, IInstanceBuilder {
		protected final Class<T> klass;

		protected final Collection<ITuple2G_<Class<?>, Object>> arguments = new ArrayList<>();

		public <X> InstanceBuilder<T> arg(Class<X> type, X value) {
			arguments.add(new Tuple2G_I<>(type, value));
			return this;
		}

		public T get() {
			try {
				final Class<?>[] types = arguments.stream().map(ITuple2G_::get0).toArray(Class[]::new);
				final Constructor<T> constructor = klass.getConstructor(types);
				final Object[] values = arguments.stream().map(ITuple2G_::get1).toArray(Object[]::new);
				return constructor.newInstance(values);
			} catch (Throwable throwable) {
				throw new RuntimeException("Could not instantiate " + this + "!", throwable);
			}
		}
	}

	protected final Collection<? extends IConsumer1<? super IInstanceBuilder>> constructors;

	public SmartInstantiator(Class<?> key, Class<S> type, Collection<? extends IConsumer1<? super IInstanceBuilder>> constructors) {
		super(key, type);
		this.constructors = constructors;
	}

	@SafeVarargs
	public SmartInstantiator(Class<?> key, Class<S> type, IConsumer1<? super IInstanceBuilder>... constructors) {
		this(key, type, HCollection.asList(constructors));
	}

	protected <_S extends S> _S instantiate(Class<_S> s) throws InstantiationException, IllegalAccessException {
		final List<Throwable> throwables = new ArrayList<>();
		for (IConsumer1<? super IInstanceBuilder> consumer : constructors) {
			final InstanceBuilder<_S> instanceBuilder = new InstanceBuilder<>(s);
			consumer.accept(instanceBuilder);
			try {
				return instanceBuilder.get();
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		throw HError.withSuppressed(new RuntimeException("Failed to find a usable constructor!"), throwables);
	}
}