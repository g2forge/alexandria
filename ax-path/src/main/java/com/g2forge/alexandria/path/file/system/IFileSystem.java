package com.g2forge.alexandria.path.file.system;

import com.g2forge.alexandria.path.path.IPath;

public interface IFileSystem<T> {
	public IPath<T> normalize(IPath<T> path);
}
