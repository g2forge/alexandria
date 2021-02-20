package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.g2forge.alexandria.java.io.RuntimeIOException;

public interface IWalkingFileVisitor extends FileVisitor<Path>, IFileTreeWalker {
	@Override
	public default Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		try {
			return Files.walkFileTree(start, options, maxDepth, this);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
