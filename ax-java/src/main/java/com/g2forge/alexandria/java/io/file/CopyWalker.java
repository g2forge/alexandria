package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Set;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class CopyWalker implements IFileTreeWalker {
	@RequiredArgsConstructor
	@Getter
	public static class Visitor extends AMultithrowFileVisitor {
		protected final CopyWalker config;

		protected final Path source;

		@Override
		protected String getMessageFile(Path path) {
			return String.format("Unable to copy %s", path);
		}

		@Override
		protected String getMessageThrow(Path start) {
			return String.format("Failed while copying %s to %s", getSource(), getConfig().getTarget());
		}

		protected Path getTarget(Path source) {
			return getConfig().getTarget().resolve(getSource().relativize(source));
		}

		@Override
		public FileVisitResult postVisitDirectory(Path directorySource, IOException exception) {
			if (exception == null) {
				// Fix up modification time of directory when done
				final Path directoryTarget = getTarget(directorySource);
				@SuppressWarnings("unlikely-arg-type")
				final boolean copyAttributes = Arrays.stream(getConfig().getOptions().apply(directoryTarget)).anyMatch(StandardCopyOption.COPY_ATTRIBUTES::equals);
				if (copyAttributes) {
					try {
						Files.setLastModifiedTime(directoryTarget, Files.getLastModifiedTime(directorySource));
					} catch (IOException exception1) {
						getThrowables().add(new RuntimeIOException(String.format("Unable to copy the modified time from %s to %s", directorySource, directoryTarget), exception1));
					}
				}
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path directorySource, BasicFileAttributes attributes) {
			// Copy the directory before visiting it's children
			final Path directoryTarget = getTarget(directorySource);
			try {
				final CopyOption[] options = getConfig().getOptions().apply(directoryTarget);
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
		public FileVisitResult visitFile(Path fileSource, BasicFileAttributes attrs) {
			final Path fileTarget = getTarget(fileSource);
			try {
				Files.copy(fileSource, fileTarget, getConfig().getOptions().apply(fileTarget));
			} catch (IOException exception) {
				throw new RuntimeIOException(String.format("Unable to copy %s to %s", fileSource, fileTarget), exception);
			}
			return FileVisitResult.CONTINUE;
		}
	}

	protected final Path target;

	protected final IFunction1<? super Path, ? extends CopyOption[]> options;

	protected Visitor constructVisitor(Path start) {
		return new Visitor(this, start);
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		try {
			return Files.walkFileTree(start, options, maxDepth, constructVisitor(start));
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
