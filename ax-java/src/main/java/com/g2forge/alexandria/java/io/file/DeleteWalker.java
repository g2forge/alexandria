package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.g2forge.alexandria.java.function.IPredicate1;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class DeleteWalker implements IFileTreeWalker {
	@RequiredArgsConstructor
	@Getter
	public static class Visitor extends AMultithrowFileVisitor {
		protected final DeleteWalker config;

		protected final Path root;

		protected final List<Path> remaining;

		@Getter(AccessLevel.PROTECTED)
		protected final Set<Path> keep = new HashSet<>();

		protected FileVisitResult delete(Path path) throws IOException {
			Files.deleteIfExists(path);
			return FileVisitResult.CONTINUE;
		}

		protected FileVisitResult deleteDirectory(Path path) throws IOException {
			final Path relative = getRoot().relativize(path);
			if (getKeep().contains(relative)) return FileVisitResult.CONTINUE;

			try {
				return delete(path);
			} catch (IOException exception) {
				return exception(path, exception);
			}
		}

		@Override
		protected FileVisitResult exception(Path value, IOException exception) {
			if (getConfig().isOnexit() && (getRemaining() != null)) {
				getRemaining().add(value);
				return FileVisitResult.CONTINUE;
			}
			return super.exception(value, exception);
		}

		protected void keep(Path directory) {
			Path current = directory;
			final Set<Path> keep = getKeep();
			while (current.getNameCount() > 1) {
				keep.add(current);
				current = current.getParent();
			}
			keep.add(current);
		}

		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException exception) throws IOException {
			if (exception != null) return exception(path, exception);
			return deleteDirectory(path);
		}

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) throws IOException {
			final Path relative = getRoot().relativize(path);
			if (getConfig().getKeep().test(relative)) {
				keep(relative);
				return FileVisitResult.SKIP_SUBTREE;
			}
			return super.preVisitDirectory(path, attributes);
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
			final Path relative = getRoot().relativize(path);
			if (getConfig().getKeep().test(relative)) {
				if (relative.getNameCount() > 1) keep(relative.getParent());
				return FileVisitResult.CONTINUE;
			}
			return delete(path);
		}
	}

	@Builder.Default
	protected final boolean onexit = true;

	@Builder.Default
	protected final IPredicate1<Path> keep = IPredicate1.create(false);

	protected Visitor constructVisitor(Path start, final List<Path> remaining) {
		return new Visitor(this, start, remaining);
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		final List<Path> remaining = isOnexit() ? new ArrayList<>() : null;
		try {
			Files.walkFileTree(start, options, maxDepth, constructVisitor(start, remaining));
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}

		if ((remaining != null) && !remaining.isEmpty()) {
			if (isOnexit()) remaining.forEach(toDelete -> toDelete.toFile().deleteOnExit());
			else throw new RuntimeIOException(String.format("Unable to delete complete directory tree! %1$s", remaining));
		}
		return start;
	}
}