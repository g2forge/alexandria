package com.g2forge.alexandria.filesystem.attributes.accessor;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.reflect.HReflection;

/**
 * A registry of all the {@link IAttributeViewAccessor} implementations supported by a file system provider. Generally an implementation of
 * {@link java.nio.file.spi.FileSystemProvider} will instantiate a single AVA registry, register all the relevant accessors, and then return that one static
 * register from {@link com.g2forge.alexandria.filesystem.AGenericFileSystemProvider#getAVARegistry()}.
 * 
 * @param <R> A reference type from which {@link IAttributeViewAccessor} instances can be constructed.
 */
public class AttributeViewAccessorRegistry<R> {
	protected final Map<String, IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>>> name = new HashMap<>();

	protected final Map<Class<?>, IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>>> attributes = new HashMap<>();

	protected final Map<Class<?>, IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>>> view = new HashMap<>();

	public Set<String> getAttributesNames() {
		return Collections.unmodifiableSet(name.keySet());
	}

	@SuppressWarnings("unchecked")
	public <A extends BasicFileAttributes> IFunction1<? super R, ? extends IAttributeViewAccessor<A, ?>> getByAttributes(Class<A> attributes) {
		return (IFunction1<? super R, ? extends IAttributeViewAccessor<A, ?>>) this.attributes.get(attributes);
	}

	/**
	 * Get the appropriate {@link IAttributeViewAccessor} factory based on the {@link java.nio.file.attribute.AttributeView#name()}. For example all file
	 * systems must support {@link HBasicFileAttributes#ATTRIBUTES_NAME "basic"}.
	 * 
	 * @param name The name of the attribute view.
	 * @return A factory to construct the appropriate {@link IAttributeViewAccessor ava} from a reference to an entry.
	 */
	public IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>> getByName(String name) {
		return this.name.get(name);
	}

	@SuppressWarnings("unchecked")
	public <V extends FileAttributeView> IFunction1<? super R, ? extends IAttributeViewAccessor<?, V>> getByView(Class<V> view) {
		return (IFunction1<? super R, ? extends IAttributeViewAccessor<?, V>>) this.view.get(view);
	}

	/**
	 * 
	 * @param factory Must support invocation with <code>null</code> reference. The resulting {@link IAttributeViewAccessor} need only support
	 *            {@link IAttributeViewAccessor#getName()} and have concrete type arguments for both type parameters.
	 * @return
	 */
	public <A extends BasicFileAttributes, V extends FileAttributeView> AttributeViewAccessorRegistry<R> register(IFunction1<? super R, ? extends IAttributeViewAccessor<A, V>> factory) {
		final IAttributeViewAccessor<A, V> created = factory.apply(null);
		final String name = created.getName();
		@SuppressWarnings("unchecked")
		final Class<A> a = (Class<A>) HReflection.getParentTypeArgument(created, IAttributeViewAccessor.class, 0);
		@SuppressWarnings("unchecked")
		final Class<V> v = (Class<V>) HReflection.getParentTypeArgument(created, IAttributeViewAccessor.class, 1);

		this.name.put(name, factory);
		this.attributes.put(a, factory);
		this.view.put(v, factory);
		return this;
	}
}