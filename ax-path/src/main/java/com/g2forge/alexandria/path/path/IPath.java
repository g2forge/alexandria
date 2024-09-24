package com.g2forge.alexandria.path.path;

import com.g2forge.alexandria.collection.ICollection;

public interface IPath<T> {
	public ICollection<T> getComponents();

	public IPath<T> resolve(IPath<T> subpath);

	public boolean isEmpty();
}
