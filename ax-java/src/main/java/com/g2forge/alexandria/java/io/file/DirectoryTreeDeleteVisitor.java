package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;

class DirectoryTreeDeleteVisitor extends SimpleFileVisitor<Path> {
	private final LinkedList<Path> onexit;

	protected DirectoryTreeDeleteVisitor(LinkedList<Path> onexit) {
		this.onexit = onexit;
	}

	protected FileVisitResult deleteAndContinue(Path path) throws IOException {
		try {
			Files.deleteIfExists(path);
		} catch (IOException exception) {
			onexit.addFirst(path);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path directory, IOException exception) throws IOException {
		if (exception == null) return deleteAndContinue(directory);
		else throw exception;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
		return deleteAndContinue(file);
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exception) throws IOException {
		return deleteAndContinue(file);
	}
}