package com.g2forge.alexandria.path.format;

import com.g2forge.alexandria.path.IPath;

public interface IPathFormat<T> {
	public String toString(IPath<T> path);

	public String toString(T component);
}
