package com.g2forge.alexandria.path.file.system;

import com.g2forge.alexandria.path.path.IPath;

public interface IFileSystem<T> {
	/**
	 * Test if the specified path goes outside of whatever root it is relative to. This can be used to verify the safety of paths from external APIs, for
	 * example.
	 * 
	 * @param path The path to test.
	 * @return {@code true} if the specified path names a file outside of the root of whatever the path will be resolved against.
	 */
	public boolean isRootEscape(IPath<T> path);

	/**
	 * Normalize the specified path. In normal operating system paths, this will resolve {@code "."} and {@code ".."} path components, with the exception of
	 * {@code ".."} components at the start of the path, which cannot be resolved without knowing the root.
	 * 
	 * @param path The path to normalize.
	 * @return The normalized representation of the path.
	 */
	public IPath<T> normalize(IPath<T> path);
}
