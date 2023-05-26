package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.g2forge.alexandria.java.core.resource.IResource;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter(AccessLevel.PROTECTED)
@ToString
public class DirectoryCreator {
	@Getter(AccessLevel.PROTECTED)
	@ToString
	@RequiredArgsConstructor
	protected static class FileCreator implements IFileCreator {
		protected final Path path;

		@Override
		public void empty() {
			try {
				Files.createFile(getPath());
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}

		@Override
		public void from(InputStream source) {
			try {
				try (final OutputStream output = Files.newOutputStream(getPath())) {
					HBinaryIO.copy(source, output);
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			} finally {
				try {
					source.close();
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			}
		}

		@Override
		public void from(Path source) {
			HFile.copy(source, getPath());
		}

	}

	public interface IFileCreator {
		public void empty();

		public void from(InputStream source);

		public default void from(IResource source) {
			from(source.getResourceAsStream(true));
		}

		public void from(Path source);
	}

	public static void create(Path path, IConsumer1<DirectoryCreator> consumer) {
		final DirectoryCreator creator = new DirectoryCreator(path.normalize());
		if (consumer != null) consumer.accept(creator);
	}

	public static void create(String path, IConsumer1<DirectoryCreator> consumer) {
		create(toPath(path), consumer);
	}

	protected static Path toPath(String string) {
		return Paths.get(string);
	}

	protected final Path path;

	protected DirectoryCreator(Path path) {
		this.path = path;
		try {
			Files.createDirectories(getPath());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public void dir(Path path, IConsumer1<DirectoryCreator> consumer) {
		final DirectoryCreator creator = new DirectoryCreator(resolve(path));
		if (consumer != null) consumer.accept(creator);
	}

	public void dir(String path, IConsumer1<DirectoryCreator> consumer) {
		dir(toPath(path), consumer);
	}

	public IFileCreator file(Path path) {
		return new FileCreator(resolve(path));
	}

	public IFileCreator file(String path) {
		return file(toPath(path));
	}

	protected Path resolve(Path path) {
		if (path.isAbsolute()) throw new IllegalArgumentException(String.format("Cannot nest an absolute path inside a directory, please call this method with a relative path instead of \"%1$s\"", path));
		final Path resolved = getPath().resolve(path).normalize();
		if (!resolved.startsWith(getPath())) throw new IllegalArgumentException(String.format("Cannot create children outside of the current directory \"%1$s\", please call this method with a path that stays inside instead of \"%2$s\"", getPath(), path));
		return resolved;
	}
}
