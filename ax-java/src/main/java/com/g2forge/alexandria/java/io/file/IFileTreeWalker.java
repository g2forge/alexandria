package com.g2forge.alexandria.java.io.file;

import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

public interface IFileTreeWalker {
	public default Path walkFileTree(Path start) {
		return walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE);
	}

	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth);
}
