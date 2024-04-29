package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IPredicate2;
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
	public enum ExtendedCopyOption implements CopyOption {
		SKIP_EXISTING;
	}

	@RequiredArgsConstructor
	@Getter
	public static class Visitor extends AMultithrowFileVisitor {
		protected final CopyWalker config;

		protected final Path source;

		protected void copyFile(Path fileSource, final Path fileTarget, final CopyOption[] options) {
			try {
				Files.copy(fileSource, fileTarget, HFile.optionFilter(options, o -> !(o instanceof ExtendedCopyOption)));
			} catch (FileAlreadyExistsException exception) {
				if (!HFile.optionEnabled(ExtendedCopyOption.SKIP_EXISTING, options)) {
					getThrowables().add(new RuntimeIOException(String.format("Unable to copy %s to %s", fileSource, fileTarget), exception));
				}
			} catch (IOException exception) {
				throw new RuntimeIOException(String.format("Unable to copy %s to %s", fileSource, fileTarget), exception);
			}
			return;
		}

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
				final boolean copyAttributes = HFile.optionEnabled(StandardCopyOption.COPY_ATTRIBUTES, getConfig().getOptions().apply(directoryTarget));
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
			// Skip the subtree if it's not included
			if (!getConfig().getInclude().test(directoryTarget, false)) return FileVisitResult.SKIP_SUBTREE;
			final CopyOption[] options = getConfig().getOptions().apply(directoryTarget);
			try {
				Files.copy(directorySource, directoryTarget, HFile.optionFilter(options, o -> !(o instanceof ExtendedCopyOption)));
			} catch (FileAlreadyExistsException | DirectoryNotEmptyException exception) {
				if (!HFile.optionEnabled(ExtendedCopyOption.SKIP_EXISTING, options)) {
					getThrowables().add(new RuntimeIOException(String.format("Unable to copy %s", directoryTarget), exception));
				}
			} catch (IOException exception) {
				getThrowables().add(new RuntimeIOException(String.format("Unable to create %s", directoryTarget), exception));
				return FileVisitResult.SKIP_SUBTREE;
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path fileSource, BasicFileAttributes attrs) {
			final Path fileTarget = getTarget(fileSource);
			if (getConfig().getInclude().test(fileTarget, true)) {
				final CopyOption[] options = getConfig().getOptions().apply(fileTarget);
				copyFile(fileSource, fileTarget, options);
			}
			return FileVisitResult.CONTINUE;
		}
	}

	protected final Path target;

	@Builder.Default
	protected final IFunction1<? super Path, ? extends CopyOption[]> options = IFunction1.create(new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES });

	@Builder.Default
	protected final IPredicate2<? super Path, ? super Boolean> include = IPredicate2.create(true);

	protected Visitor constructVisitor(Path start) {
		return new Visitor(this, start);
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		return constructVisitor(start).walkFileTree(start, options, maxDepth);
	}
}
