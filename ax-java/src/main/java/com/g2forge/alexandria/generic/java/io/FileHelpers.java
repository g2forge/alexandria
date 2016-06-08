package com.g2forge.alexandria.generic.java.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileHelpers {
	public static void delete(final Path path) {
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult postVisitDirectory(final Path path, final IOException exception) throws IOException {
					if (exception != null) throw exception;
					Files.delete(path);
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
					try {
						Files.delete(path);
					} catch (final NoSuchFileException exception) {}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (final NoSuchFileException exception) {} catch (final IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
