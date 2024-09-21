package com.g2forge.alexandria.path.directory;

import com.g2forge.alexandria.path.IPath;

public interface IDirectorySystem<T> {
	public IPath<T> normalize(IPath<T> path);
}
