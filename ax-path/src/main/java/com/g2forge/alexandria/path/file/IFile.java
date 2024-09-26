package com.g2forge.alexandria.path.file;

import com.g2forge.alexandria.path.file.system.IFileSystem;

public interface IFile<T> {
	public IFile<T> get(T filename);

	public IFileSystem<T> getFileSystem();

	public IFile<T> getParent();

	public boolean isDirectory();
}
