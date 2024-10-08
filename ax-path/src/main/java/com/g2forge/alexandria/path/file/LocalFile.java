package com.g2forge.alexandria.path.file;

import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.path.file.system.IFileSystem;
import com.g2forge.alexandria.path.file.system.OSFileSystem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class LocalFile implements IFile<String> {
	protected final Path path;

	public LocalFile(Path path) {
		this.path = path.normalize();
	}

	@Override
	public IFile<String> get(String filename) {
		final Path resolved = getPath().resolve(filename);
		if (!Files.exists(resolved)) return null;
		return new LocalFile(resolved);
	}

	@Override
	public IFileSystem<String> getFileSystem() {
		return OSFileSystem.getFileSystem();
	}

	@Override
	public IFile<String> getParent() {
		return new LocalFile(getPath().getParent());
	}

	@Override
	public boolean isDirectory() {
		return Files.isDirectory(getPath());
	}
}
