package com.g2forge.alexandria.path.file;

import com.g2forge.alexandria.path.directory.IDirectorySystem;

public interface IFile<T> {
	public IDirectorySystem<T> getDirectorySystem();

	public IFile<T> getParent();

	public IFile<T> get(T filename);

	public boolean isDirectory();
}
