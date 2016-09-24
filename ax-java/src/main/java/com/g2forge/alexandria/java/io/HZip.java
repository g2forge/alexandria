package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.g2forge.alexandria.java.marker.Helpers;

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

		final byte[] buffer = new byte[8192];
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
				} catch (Exception exception) {
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
}
