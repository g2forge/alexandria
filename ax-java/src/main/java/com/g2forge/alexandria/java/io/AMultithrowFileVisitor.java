package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.g2forge.alexandria.java.core.error.HError;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class AMultithrowFileVisitor implements FileVisitor<Path> {
	@Getter(AccessLevel.PROTECTED)
	protected final Collection<Throwable> throwables = new ArrayList<>();

	protected void add(Throwable throwable) {
		getThrowables().add(throwable);
	}

	protected String getMessageFile(Path path) {
		return String.format("Failed while processing %s", path);
	}

	protected String getMessageThrow(Path start) {
		return String.format("Failed while processing %s", start);
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path value, IOException exception) {
		if (exception instanceof FileSystemLoopException) add(new RuntimeIOException(String.format("Cyclic directory structure detected in %s", value), exception));
		else add(new RuntimeIOException(getMessageFile(value), exception));
		return FileVisitResult.CONTINUE;
	}

	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		Path retVal = null;
		try {
			retVal = Files.walkFileTree(start, options, maxDepth, this);
		} catch (IOException exception) {
			getThrowables().add(exception);
		}

		// If there were any exceptions, then report them!
		if (!getThrowables().isEmpty()) throw HError.addSuppressed(new RuntimeIOException(getMessageThrow(start)), getThrowables());
		return retVal;
	}
}