package com.g2forge.alexandria.path.path.format;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.path.path.IPath;

public interface IPathFormat<T, P extends IPath<T>> {
	public T toComponent(String component);

	public P toPath(ICollection<T> components);

	public P toPath(String path);

	public String toString(IPath<T> path);

	public String toString(T component);
}
