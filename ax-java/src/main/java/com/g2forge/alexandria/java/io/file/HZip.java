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
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;

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
	 * Compare the contents of several zipfiles or Jar files.
	 * 
	 * @param paths Paths to the zip files to compare.
	 * @return <code>true</code> if all the zipfiles contain exactly the same contents.
	 */
	public static boolean isEqual(Path... paths) {
		return isEqual(HCollection.asList(paths));
	}

	/**
	 * Compare the contents of several zipfiles or Jar files.
	 * 
	 * @param paths Paths to the zip files to compare.
	 * @return <code>true</code> if all the zipfiles contain exactly the same contents.
	 */
	public static boolean isEqual(List<Path> paths) {
		if (paths.size() < 2) return true;

		final List<ZipFile> files = paths.stream().map(p -> {
			try {
				return new ZipFile(p.toFile());
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}).collect(Collectors.toList());
		try (final ICloseable closeFiles = () -> HIO.closeAll(files)) {
			for (Enumeration<? extends ZipEntry> enuemration = files.get(0).entries(); enuemration.hasMoreElements();) {
				final String name = enuemration.nextElement().getName();

				final List<InputStream> streams = files.stream().map(zip -> {
					final ZipEntry entry = zip.getEntry(name);
					if (entry != null) {
						try {
							return zip.getInputStream(entry);
						} catch (IOException e) {
							throw new RuntimeIOException(e);
						}
					} else return null;
				}).collect(Collectors.toList());
				try (final ICloseable closeStreams = () -> HIO.closeAll(streams)) {
					// If any of the zipfiles don't have the entry, fail
					if (streams.contains(null)) return false;
					// Make sure all the entries have the same data
					if (!HIO.isEqual(streams)) return false;
				}
			}
		}
		return true;
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
