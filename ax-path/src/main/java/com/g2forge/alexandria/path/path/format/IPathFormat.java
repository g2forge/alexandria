package com.g2forge.alexandria.path.path.format;

import com.g2forge.alexandria.path.path.IPath;

public interface IPathFormat<T> {
	public T toComponent(String component);

	public IPath<T> toPath(String path);

	public String toString(IPath<T> path);

	public String toString(T component);
}
