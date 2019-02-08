package com.g2forge.alexandria.filesystem.file;

import com.g2forge.alexandria.filesystem.path.IGenericFileSystemInternal;

/**
 * Provides {@link GenericEntryReference} access to the internal representation of a file system. Implementations of this class can generally be stateless
 * singletons.
 *
 * @param <E> The generic type of entries in the file system. Conceptually a parent type of <code>F</code> and <code>D</code>, though this is not required.
 * @param <F> The generic type of files in the file system.
 * @param <D> The generic type of directories in the file system.
 * @param <P> The generic type of paths in the file system.
 */
public interface IGenericEntryAccessor<E, F, D, P> {
	/**
	 * Cast the specified entry to a directory, and fail if it is not. This may not be a literal java cast depending on the underlying types.
	 * 
	 * @param entry The entry
	 * @return A directory representation of the entry.
	 * @see #asEntry(Object)
	 * @see IGenericEntryAccessor#isDirectory(Object)
	 */
	public D asDirectory(E entry);

	/**
	 * Cast the specified directory to an entry. This may not be a literal java cast depending on the underlying types. This method must be the inverse of
	 * {@link #asDirectory(Object)}.
	 * 
	 * @param directory The directory
	 * @return An entry representation of the directory.
	 */
	public E asEntry(D directory);

	/**
	 * Cast the specified entry to a file, and fail if it is not. This may not be a literal java cast depending on the underlying types.
	 * 
	 * @param entry The entry
	 * @return A file representation of the entry.
	 * @see #isFile(Object)
	 */
	public F asFile(E entry);

	/**
	 * Get the entry with the specified name from the directory. Note that this method need not handle parent and self names like ".." and ".". It should assume
	 * those have been resolved externally, and it's behavior in those cases is undefined.
	 * 
	 * @param directory The directory.
	 * @param name The name of the entry to return.
	 * @return The entry with the specified name or <code>null</code> if there is no such entry.
	 */
	public E getEntry(D directory, String name);

	/**
	 * Get the generic file system internal for the specified path. This is necessary to access information like the
	 * {@link IGenericFileSystemInternal#getParent()} and {@link IGenericFileSystemInternal#getSelf()} path names.
	 * 
	 * @param path The path.
	 * @return The file system internal for the specified path.
	 */
	public IGenericFileSystemInternal<P> getGenericFileSystemInternal(P path);

	/**
	 * Test if the specified entry is a directory.
	 * 
	 * @param entry The entry to test
	 * @return <code>true</code> if this entry is a directory, and can be converted by {@link #asDirectory(Object)} without an exception.
	 * @see #asDirectory(Object)
	 * @see #asEntry(Object)
	 */
	public boolean isDirectory(E entry);

	/**
	 * Test if the specified entry is a file.
	 * 
	 * @param entry The entry to test
	 * @return <code>true</code> if this entry is a file, and can be converted by {@link #asFile(Object)} without an exception.
	 * @see #asFile(Object)
	 */
	public boolean isFile(E entry);
}
