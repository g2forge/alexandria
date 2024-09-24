package com.g2forge.alexandria.path.file;

import com.g2forge.alexandria.path.file.system.IFileSystem;

public interface IFile<T> {
	public IFileSystem<T> getDirectorySystem();

	public IFile<T> getParent();

	public IFile<T> get(T filename);

	public boolean isDirectory();
}
