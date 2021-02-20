package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class AMultithrowFileVisitor extends SimpleFileVisitor<Path> implements IWalkingFileVisitor {
	@Getter(AccessLevel.PROTECTED)
	protected final Collection<Throwable> throwables = new ArrayList<>();

	protected void add(Throwable throwable) {
		getThrowables().add(throwable);
	}

	protected FileVisitResult exception(Path value, IOException exception) {
		Objects.requireNonNull(value);
		if (exception instanceof FileSystemLoopException) add(new RuntimeIOException(String.format("Cyclic directory structure detected in %s", value), exception));
		else add(new RuntimeIOException(getMessageFile(value), exception));
		return FileVisitResult.CONTINUE;
	}

	protected String getMessageFile(Path path) {
		return String.format("Failed while processing %s", path);
	}

	protected String getMessageThrow(Path start) {
		return String.format("Failed while processing %s", start);
	}

	@Override
	public FileVisitResult visitFileFailed(Path value, IOException exception) {
		return exception(value, exception);
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		Path retVal = null;
		try {
			retVal = Files.walkFileTree(start, options, maxDepth, this);
		} catch (IOException exception) {
			getThrowables().add(exception);
		}

		// If there were any exceptions, then report them!
		if (!getThrowables().isEmpty()) throw HError.withSuppressed(new RuntimeIOException(getMessageThrow(start)), getThrowables());
		return retVal;
	}
}