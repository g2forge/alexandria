package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.adt.attributes.IHasExplanation;
import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_O;
import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.error.OrThrowable;
import com.g2forge.alexandria.java.core.helpers.HBinary;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.HTextIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@Builder
public class CompareWalker implements IFileTreeWalker {
	@Data
	@RequiredArgsConstructor
	public static class DirectoryMismatch implements IMismatch {
		protected final Path relative;

		@Singular
		protected final Map<OrThrowable<List<String>>, Set<Path>> contents;

		@Override
		public String getExplanation() {
			final StringBuilder retVal = new StringBuilder();
			for (Map.Entry<OrThrowable<List<String>>, Set<Path>> entry : getContents().entrySet()) {
				retVal.append(entry.getValue()).append(": ");
				if (entry.getKey().isEmpty()) retVal.append(HError.toString(entry.getKey().getThrowable()));
				else retVal.append(entry.getKey().get());
			}
			return retVal.toString();
		}
	}

	@Data
	@RequiredArgsConstructor
	public static class FileMismatch implements IMismatch {
		protected final Path relative;

		protected final boolean isText;

		@Singular
		protected final Map<OrThrowable<String>, Set<Path>> contents;

		@Override
		public String getExplanation() {
			final StringBuilder retVal = new StringBuilder();
			for (Map.Entry<OrThrowable<String>, Set<Path>> entry : getContents().entrySet()) {
				retVal.append(entry.getValue()).append(": ");
				if (entry.getKey().isEmpty()) retVal.append(HError.toString(entry.getKey().getThrowable()));
				else {
					retVal.append(entry.getKey().get());
					if (isText) {
						retVal.append(": ");
						try (final InputStream stream = Files.newInputStream(HCollection.getAny(entry.getValue()).resolve(relative))) {
							retVal.append(HTextIO.readAll(stream, true));
						} catch (IOException e) {
							retVal.append(HError.toString(e));
						}
					}
					retVal.append("\n");
				}
			}
			return retVal.toString();
		}
	}

	public interface IMismatch extends IHasExplanation<String> {
		public Path getRelative();
	}

	public static class MismatchError extends Error {
		private static final long serialVersionUID = -7307790845007062634L;

		protected static String createMessage(IMismatch mismatch) {
			return String.format("\"%1$s\" is not the same across all root directories:\n%2$s", mismatch.getRelative(), mismatch.getExplanation());
		}

		@Getter
		protected final IMismatch mismatch;

		public MismatchError(IMismatch mismatch) {
			super(createMessage(mismatch));
			this.mismatch = mismatch;
		}

		public MismatchError(IMismatch mismatch, Exception cause) {
			super(createMessage(mismatch), cause);
			this.mismatch = mismatch;
		}
	}

	@RequiredArgsConstructor
	@Getter
	public static class Visitor extends AMultithrowFileVisitor {
		protected final CompareWalker config;

		protected final Path start;

		protected final List<Path> roots;

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) {
			final Path relative = getStart().relativize(path);
			final Map<OrThrowable<List<String>>, Set<Path>> grouped = getRoots().stream().map(p -> {
				Stream<Path> list = null;
				try {
					try {
						list = Files.list(p.resolve(relative));
					} catch (IOException exception) {
						return new Tuple2G_O<>(p, new OrThrowable<List<String>>(exception));
					}
					return new Tuple2G_O<>(p, new OrThrowable<List<String>>(list.map(Path::getFileName).map(Object::toString).sorted().collect(Collectors.toList())));
				} finally {
					if (list != null) list.close();
				}
			}).collect(Collectors.groupingBy(ITuple2G_::get1, Collectors.mapping(ITuple2G_::get0, Collectors.toSet())));
			if (grouped.size() > 1) throw new MismatchError(new DirectoryMismatch(relative, grouped));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
			final Path relative = getStart().relativize(path);
			final boolean isText = getConfig().getIsTextFunction().apply(relative);
			final Map<OrThrowable<String>, Set<Path>> grouped = getRoots().stream().map(p -> {
				try {
					final String hash;
					if (isText) {
						try (final InputStream stream = Files.newInputStream(path)) {
							hash = HBinary.toHex(HIO.sha1(HTextIO.readAll(stream, true)));
						}
					} else hash = HBinary.toHex(HIO.sha1(p.resolve(relative), Files::newInputStream));

					return new Tuple2G_O<>(p, new OrThrowable<String>(hash));
				} catch (RuntimeIOException exception) {
					return new Tuple2G_O<>(p, new OrThrowable<String>(exception.getCause()));
				} catch (IOException exception) {
					return new Tuple2G_O<>(p, new OrThrowable<String>(exception));
				}
			}).collect(Collectors.groupingBy(ITuple2G_::get1, Collectors.mapping(ITuple2G_::get0, Collectors.toSet())));
			if (grouped.size() > 1) throw new MismatchError(new FileMismatch(relative, isText, grouped));
			return FileVisitResult.CONTINUE;
		}
	}

	@Singular
	protected final List<Path> roots;

	@Default
	protected final IFunction1<? super Path, ? extends Boolean> isTextFunction = IFunction1.create(false);

	protected Visitor constructVisitor(Path start) {
		return new Visitor(this, start, HCollection.concatenate(HCollection.asList(start), getRoots()));
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
