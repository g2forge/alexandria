package com.g2forge.alexandria.filesystem.attributes.accessor;

import java.nio.file.NoSuchFileException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.util.Set;

import com.g2forge.alexandria.java.adt.name.IStringNamed;

/**
 * This interface provides access to all of a related group of attributes. At the a minimum you will need to implement this for
 * {@link java.nio.file.attribute.BasicFileAttributes} and {@link java.nio.file.attribute.BasicFileAttributeView}. When implementing a file system by extending
 * {@link com.g2forge.alexandria.filesystem.AGenericFileSystemProvider}, implement as many instances of this interface as necessary for all of your attributes,
 * and register them with the {@link AttributeViewAccessorRegistry} returned by
 * {@link com.g2forge.alexandria.filesystem.AGenericFileSystemProvider#getAVARegistry()}.
 * 
 * An instance of this interface should be bound to a specific {@link AGenericFileSystemProvider entry}, however no access to this entry may be attempted until
 * one of the modifier methods is called. This is necessary as {@link AttributeViewAccessorRegistry} will create instance of this interface bound to a
 * <code>null</code> reference in order to query the type parameters and {@link #getName()}.
 * 
 * @param <A> The type of the attributes. Must match <code>V</code>.
 * @param <V> The type of the attributes view. Must match <code>A</code>.
 */
public interface IAttributeViewAccessor<A extends BasicFileAttributes, V extends FileAttributeView> extends IStringNamed {
	/**
	 * Get a specific attribute value.
	 * 
	 * @param name The name of the attribute (see {@link #getNames()}) to query.
	 * @return The value of the attribute.
	 * @throws NoSuchFileException If the entry this accessor is bound to does not exist.
	 */
	public Object get(String name) throws NoSuchFileException;

	/**
	 * Get the attributes.
	 * 
	 * @return The attributes.
	 * @throws NoSuchFileException If the entry this accessor is bound to does not exist.
	 */
	public A getAttributes() throws NoSuchFileException;

	/**
	 * The name of this attribute group such as "basic". This corresponds to
	 * {@link com.g2forge.alexandria.filesystem.attributes.FileAttributeName#getAttributes()}.
	 * 
	 * @return The name of these attributes such as "basic".
	 */
	public String getName();

	/**
	 * Get the names of the attributes supported by this attribute group. These corresponds to
	 * {@link com.g2forge.alexandria.filesystem.attributes.FileAttributeName#getAttribute()} and can be used as the arguments to {@link #get(String)} and
	 * {@link #set(String, Object)}.
	 * 
	 * @return
	 */
	public Set<String> getNames();

	/**
	 * Get the attributes view. Note that unlike {@link #getAttributes()} this method does not actually access the file system. Instead it will be the accessor
	 * methods on the return value which do so. As a result the caller cannot assume that the entry exists just because this method succeeds.
	 * 
	 * @return The attributes view.
	 */
	public V getView();

	/**
	 * Set a specific attribute value.
	 * 
	 * @param name The name of the attribute (see {@link #getNames()}) to query.
	 * @param value The value of the attribute.
	 * @throws NoSuchFileException If the entry this accessor is bound to does not exist.
	 */
	public void set(String name, Object value) throws NoSuchFileException;
}