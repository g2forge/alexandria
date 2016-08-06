package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor class DirectoryTreeCopyVisitor implements FileVisitor<Path> {
	protected final Path source;

	protected final Path target;

	protected final boolean preserve;

	protected final Function<Path, Boolean> overwrite;

	@Getter
	protected final Collection<Throwable> throwables = new ArrayList<>();

	protected Path getTarget(Path source) {
		return this.target.resolve(this.source.relativize(source));
	}

	@Override
	public FileVisitResult postVisitDirectory(Path directorySource, IOException exception) {
		// Fix up modification time of directory when done
		if ((exception == null) && preserve) {
			final Path directoryTarget = getTarget(directorySource);
			try {
				Files.setLastModifiedTime(directoryTarget, Files.getLastModifiedTime(directorySource));
			} catch (IOException exception1) {
				getThrowables().add(new RuntimeIOException(String.format("Unable to copy the modified time from %s to %s", directorySource, directoryTarget), exception1));
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path directorySource, BasicFileAttributes attributes) {
		// Copy the directory before visiting it's children
		final CopyOption[] options = (preserve) ? new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES } : new CopyOption[0];
		final Path directoryTarget = getTarget(directorySource);
		try {
			Files.copy(directorySource, directoryTarget, options);
		} catch (FileAlreadyExistsException exception) {
			// Ignore it if the directory already exists, since that's not an issue
		} catch (IOException exception) {
			getThrowables().add(new RuntimeIOException(String.format("Unable to create %s", directoryTarget), exception));
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		FileHelpers.copyFile(file, getTarget(file), preserve, overwrite);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException exception) {
		if (exception instanceof FileSystemLoopException) getThrowables().add(new RuntimeIOException(String.format("Cyclic directory structure detected in %s", path), exception));
		else getThrowables().add(new RuntimeIOException(String.format("Unable to copy %s", path), exception));
		return FileVisitResult.CONTINUE;
	}
}