package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class DirectoryTreeCopyVisitor extends AMultithrowFileVisitor {
	protected final Path source;

	protected final Path target;

	protected final boolean preserve;

	protected final Function<Path, Boolean> overwrite;

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
		HFile.copyFile(file, getTarget(file), preserve, overwrite);
		return FileVisitResult.CONTINUE;
	}

	protected String getMessageFile(Path path) {
		return String.format("Unable to copy %s", path);
	}

	protected String getMessageThrow(Path start) {
		return String.format("Failed while copying %s to %s", source, target);
	}
}