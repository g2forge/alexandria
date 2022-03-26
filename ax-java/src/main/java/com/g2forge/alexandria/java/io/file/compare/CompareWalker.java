package com.g2forge.alexandria.java.io.file.compare;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
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
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.LiteralFunction1;
import com.g2forge.alexandria.java.io.file.AMultithrowFileVisitor;
import com.g2forge.alexandria.java.io.file.IFileTreeWalker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class CompareWalker implements IFileTreeWalker {
	@Data
	@RequiredArgsConstructor
	public static class DirectoryMismatch implements IMismatch {
		protected final Path relative;

		/** Map from directory contents (or exception) to the directories that contain those contents. */
		protected final Map<OrThrowable<List<String>>, Set<Path>> contents;

		@Override
		public String getExplanation() {
			final StringBuilder retVal = new StringBuilder();
			retVal.append("Below are groups of input directories and the contents of those directories\n");
			for (Map.Entry<OrThrowable<List<String>>, Set<Path>> entry : getContents().entrySet()) {
				retVal.append(entry.getValue()).append(": ");
				if (entry.getKey().isEmpty()) retVal.append(HError.toString(entry.getKey().getThrowable()));
				else retVal.append(entry.getKey().get()).append("\n");
			}
			return retVal.toString();
		}
	}

	@Data
	@RequiredArgsConstructor
	public static class FileMismatch implements IMismatch {
		protected final Path relative;

		protected final Map<IFileCompareGroup, Set<Path>> contents;

		@Override
		public String getExplanation() {
			final StringBuilder retVal = new StringBuilder();
			retVal.append("Below are groups of input files and a description of the contents of those files\n");
			for (Map.Entry<IFileCompareGroup, Set<Path>> entry : getContents().entrySet()) {
				retVal.append(entry.getValue()).append(": ").append(entry.getKey().describe(entry.getValue(), getRelative())).append("\n");
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
			return String.format("Entry at path \"%1$s\" is not the same across all root directories:\n%2$s", mismatch.getRelative(), mismatch.getExplanation());
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

		protected <T> Map<IFileCompareGroup, Set<Path>> group(final Path relative, final IFileCompareGroupFunction<T> groupFunction) {
			final Map<Path, T> hashes = new LinkedHashMap<>();
			for (Path p : getRoots()) {
				final Path resolved = p.resolve(relative);
				final T hashed = groupFunction.hash(resolved);
				hashes.put(p, hashed);
			}
			return groupFunction.group(hashes);
		}

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
			final IFileCompareGroupFunction<?> groupFunction = getConfig().getGroupFunctionFunction().apply(relative);
			final Map<IFileCompareGroup, Set<Path>> grouped = group(relative, groupFunction);
			if (grouped.size() > 1) throw new MismatchError(new FileMismatch(relative, grouped));
			return FileVisitResult.CONTINUE;
		}
	}

	@Singular
	protected final List<Path> roots;

	@Default
	protected final IFunction1<? super Path, ? extends IFileCompareGroupFunction<?>> groupFunctionFunction = new LiteralFunction1<>(SHA1HashFileCompareGroupFunction.create());

	protected Visitor constructVisitor(Path start) {
		return new Visitor(this, start, HCollection.concatenate(HCollection.asList(start), getRoots()));
	}

	@Override
	public Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth) {
		return constructVisitor(start).walkFileTree(start, options, maxDepth);
	}
}
