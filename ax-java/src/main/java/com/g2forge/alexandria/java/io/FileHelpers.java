package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.function.Function;

import com.g2forge.alexandria.java.concurrent.ConcurrentHelpers;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class FileHelpers {
	public static void copy(Path source, Path target, boolean preserve, Function<Path, Boolean> overwrite) {
		final Path destination = Files.isDirectory(target) ? target.resolve(source.getFileName()) : target;

		final DirectoryTreeCopyVisitor visitor = new DirectoryTreeCopyVisitor(source, destination, preserve, overwrite);
		try {
			Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, visitor);
		} catch (IOException exception) {
			visitor.getThrowables().add(exception);
		}

		// If there were any exceptions, then report them!
		if (!visitor.getThrowables().isEmpty()) {
			final RuntimeIOException toThrow = new RuntimeIOException(String.format("Error while copying %s to %s", source, target));
			for (Throwable throwable : visitor.getThrowables())
				toThrow.addSuppressed(throwable);
			throw toThrow;
		}
	}

	public static void copyFile(Path source, Path target, boolean preserve, Function<Path, Boolean> overwrite) {
		final CopyOption[] options = preserve ? new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING } : new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };
		if (Files.notExists(target) || overwrite.apply(target)) {
			try {
				Files.copy(source, target, options);
			} catch (IOException exception) {
				throw new RuntimeIOException(String.format("Unable to copy %s to %s", source, target), exception);
			}
		}
	}

	public static void delete(Path path) throws IOException {
		final LinkedList<Path> onexit = new LinkedList<>();
		Files.walkFileTree(path, new DirectoryTreeDeleteVisitor(onexit));
		onexit.forEach(toDelete -> toDelete.toFile().deleteOnExit());
	}

	public static void gc() {
		gc(3, 100);
	}

	public static void gc(int repeat, int pause) {
		for (int i = 0; i < repeat; i++) {
			if (i > 0) ConcurrentHelpers.wait(FileHelpers.class, pause);
			System.gc();
		}
	}
}
