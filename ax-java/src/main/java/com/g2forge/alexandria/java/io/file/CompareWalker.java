package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.LiteralFunction1;
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
	public static class BinaryHashFunction implements IFunction1<Path, String>, ISingleton {
		protected static final BinaryHashFunction INSTANCE = new BinaryHashFunction();

		public static BinaryHashFunction create() {
			return INSTANCE;
		}
		
		@Override
		public String apply(Path path) {
			return HBinary.toHex(HIO.sha1(path, Files::newInputStream));
		}
	}

	@Data
	@RequiredArgsConstructor
	public static class DirectoryMismatch implements IMismatch {
		protected final Path relative;

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

		protected final IFunction1<? super Path, ? extends String> hashFunction;

		protected final Map<OrThrowable<String>, Set<Path>> contents;

		@Override
		public String getExplanation() {
			final StringBuilder retVal = new StringBuilder();
			for (Map.Entry<OrThrowable<String>, Set<Path>> entry : getContents().entrySet()) {
				retVal.append(entry.getValue()).append(": ");
				if (entry.getKey().isEmpty()) retVal.append(HError.toString(entry.getKey().getThrowable()));
				else {
					retVal.append(entry.getKey().get());
					if (hashFunction instanceof IHelpfulHashFunction) {
						retVal.append(": ");
						retVal.append(((IHelpfulHashFunction) hashFunction).explain(HCollection.getAny(entry.getValue()).resolve(relative)));
					}
					retVal.append("\n");
				}
			}
			return retVal.toString();
		}
	}

	public interface IHelpfulHashFunction extends IFunction1<Path, String> {
		public String explain(Path path);
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

	public static class TextHashFunction implements IHelpfulHashFunction, ISingleton {
		protected static final TextHashFunction INSTANCE = new TextHashFunction();

		public static TextHashFunction create() {
			return INSTANCE;
		}

		@Override
		public String apply(Path path) {
			try (final InputStream stream = Files.newInputStream(path)) {
				return HBinary.toHex(HIO.sha1(HTextIO.readAll(stream, true)));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public String explain(Path path) {
			try (final InputStream stream = Files.newInputStream(path)) {
				return HTextIO.readAll(stream, true);
			} catch (IOException e) {
				return HError.toString(e);
			}
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
			final IFunction1<? super Path, ? extends String> hashFunction = getConfig().getHashFunctionFunction().apply(relative);
			final Map<OrThrowable<String>, Set<Path>> grouped = getRoots().stream().map(p -> {
				try {
					final Path resolved = p.resolve(relative);
					final String hash = hashFunction.apply(resolved);
					return new Tuple2G_O<>(p, new OrThrowable<String>(hash));
				} catch (RuntimeIOException | UncheckedIOException exception) {
					return new Tuple2G_O<>(p, new OrThrowable<String>(exception.getCause()));
				} catch (Throwable exception) {
					return new Tuple2G_O<>(p, new OrThrowable<String>(exception));
				}
			}).collect(Collectors.groupingBy(ITuple2G_::get1, Collectors.mapping(ITuple2G_::get0, Collectors.toSet())));
			if (grouped.size() > 1) throw new MismatchError(new FileMismatch(relative, hashFunction, grouped));
			return FileVisitResult.CONTINUE;
		}
	}

	@Singular
	protected final List<Path> roots;

	@Default
	protected final IFunction1<? super Path, ? extends IFunction1<? super Path, ? extends String>> hashFunctionFunction = new LiteralFunction1<>(BinaryHashFunction.create());

	protected Visitor constructVisitor(Path start) {
		return new Visitor(this, start, HCollection.concatenate(HCollection.asList(start), getRoots()));
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		return constructVisitor(start).walkFileTree(start, options, maxDepth);
	}
}
