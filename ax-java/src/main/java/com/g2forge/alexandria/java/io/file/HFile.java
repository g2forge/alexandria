package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HFile {
	public static void copy(Path source, Path target, boolean preserve, Function<Path, Boolean> overwrite) {
		final Path destination = Files.isDirectory(target) ? target.resolve(source.getFileName()) : target;
		final DirectoryTreeCopyVisitor visitor = new DirectoryTreeCopyVisitor(source, destination, preserve, overwrite);
		visitor.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE);
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
			if (i > 0) HConcurrent.wait(HFile.class, pause);
			System.gc();
		}
	}

	public static List<Path> toList(DirectoryStream<Path> stream) {
		try {
			final List<Path> retVal = new ArrayList<>();
			for (Path path : stream) {
				retVal.add(path);
			}
			return retVal;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}
	}
}
