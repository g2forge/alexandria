package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IPredicate1;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.with.CollectionWithExplanation;
import com.g2forge.alexandria.java.with.CollectionWithExplanation.CollectionWithExplanationBuilder;
import com.g2forge.alexandria.java.with.IWithExplanation;
import com.g2forge.alexandria.java.with.SimpleWithExplanation;

import lombok.Data;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HZip {
	@Data
	public static class Entry {
		protected final Path root;

		protected final Path absolute;
	}

	public static void zip(Path target, Function<? super Entry, ? extends String> function, Iterable<? extends Path> sources) {
		if (function == null) function = entry -> entry.getRoot().relativize(entry.getAbsolute()).toString();

		final byte[] buffer = new byte[HIO.getRecommendedBufferSize()];
		try (final ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(target))) {
			final Collection<Entry> entries = new ArrayList<>();
			for (Path source : sources) {
				Files.walk(source).forEach(path -> {
					if (path.equals(target)) return;
					if (Files.isDirectory(path)) return;
					entries.add(new Entry(source, path));
				});
			}

			for (Entry entry : entries) {
				try {
					out.putNextEntry(new ZipEntry(function.apply(entry)));
					try (final InputStream in = Files.newInputStream(entry.getAbsolute())) {
						int len;
						while ((len = in.read(buffer)) > 0)
							out.write(buffer, 0, len);
					}
				} catch (IOException exception) {
					throw new RuntimeIOException(String.format("Failed to create zip entry for %s", entry.getAbsolute()), exception);
				}
			}

			out.closeEntry();
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	public static void zip(Path target, Function<Entry, String> function, Path... sources) {
		zip(target, function, Arrays.asList(sources));
	}

	/**
	 * Compare the contents of several zip or jar files.
	 * 
	 * @param equalsFactory A function to create a stream comparator from the zip entry filename. Can be {@code null}.
	 * @param paths Paths to the zip files to compare.
	 * @return {@code true} if all the zipfiles contain exactly the same contents.
	 */
	public static IWithExplanation<Boolean, Collection<String>> isEqual(IFunction1<? super String, ? extends IPredicate1<? super Collection<? extends InputStream>>> equalsFactory, Path... paths) {
		return isEqual(equalsFactory, HCollection.asList(paths));
	}

	/**
	 * Compare the contents of several zip or jar files.
	 * 
	 * @param equalsFactory A function to create a stream comparator from the zip entry filename. Can be {@code null}.
	 * @param paths Paths to the zip files to compare.
	 * @return {@code true} if all the zipfiles contain exactly the same contents. The explanation will be a (possibly incomplete) list of mismatches if the
	 *         return value is {@code false}.
	 */
	public static IWithExplanation<Boolean, Collection<String>> isEqual(IFunction1<? super String, ? extends IPredicate1<? super Collection<? extends InputStream>>> equalsFactory, List<Path> paths) {
		if (equalsFactory == null) equalsFactory = name -> HBinaryIO::isEqual;
		if (paths.size() < 2) return new SimpleWithExplanation<>(true, null);

		final CollectionWithExplanationBuilder<Boolean, String> retVal = CollectionWithExplanation.<Boolean, String>builder().value(true);
		final List<ZipFile> files = new ArrayList<>(paths.size());
		try (final ICloseable closeFiles = () -> HIO.closeAll(files)) {
			// Add the files inside the try/finally to ensure they all get closed
			for (Path path : paths) {
				try {
					files.add(new ZipFile(path.toFile()));
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			}

			final int size = files.get(0).size();
			for (int i = 1; i < files.size(); i++) {
				if (files.get(i).size() != size) retVal.value(false).explanation(String.format("\"%1$s\" has %2$d entries, whereas \"%3$s\" has %4$d!", paths.get(0), size, paths.get(i), files.get(i).size()));
			}

			for (Enumeration<? extends ZipEntry> enuemration = files.get(0).entries(); enuemration.hasMoreElements();) {
				final String name = enuemration.nextElement().getName();

				final List<InputStream> streams = new ArrayList<>(files.size());
				try (final ICloseable closeStreams = () -> HIO.closeAll(streams)) {
					// Open the streams inside the try/finally to be sure they all get closed
					for (int i = 0; i < paths.size(); i++) {
						final ZipFile file = files.get(i);
						final ZipEntry entry = file.getEntry(name);

						// If any of the zipfiles don't have the entry, fail
						if (entry == null) retVal.value(false).explanation(String.format("\"%1$s\" has entry \"%2$s\", which isn't present in \"%3$s\"!", paths.get(0), name, paths.get(i)));

						try {
							streams.add(file.getInputStream(entry));
						} catch (IOException e) {
							throw new RuntimeIOException(e);
						}
					}

					// Make sure all the entries have the same data
					if (!equalsFactory.apply(name).test(streams)) retVal.value(false).explanation(String.format("Entry \"%1$s\", is not the same across archives!", name));
				}
			}
		}
		return retVal.build();
	}

	/**
	 * Unzip an zip file to the specified destination.
	 * 
	 * @param zipfile The zipfile to unzip.
	 * @param destination The destination directory, which will hold the contents of the zip file. Existing contents will be overwritten.
	 */
	public void unzip(Path zipfile, Path destination) {
		final Path absoluteDestination = destination.toAbsolutePath().normalize();
		try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipfile))) {
			for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
				final Path child = destination.resolve(zipEntry.getName()).normalize();
				if (!child.toAbsolutePath().startsWith(absoluteDestination)) throw new RuntimeIOException(String.format("Entry \"%1$s\" would be outside of the destination directory!", child));
				if (zipEntry.isDirectory()) Files.createDirectories(child);
				else try (final OutputStream out = Files.newOutputStream(child)) {
					HBinaryIO.copy(zipInputStream, out);
				}
			}
			zipInputStream.closeEntry();
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
