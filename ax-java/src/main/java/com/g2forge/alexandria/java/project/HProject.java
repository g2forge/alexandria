package com.g2forge.alexandria.java.project;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.helpers.HRuntime;
import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HProject {
	public static final String JAR = "jar";

	public static final String POM = "pom.xml";

	protected static FileSystem openFileSystem(URI uri) throws IOException {
		try {
			FileSystems.getFileSystem(uri);
			return null;
		} catch (FileSystemNotFoundException exception) {
			return FileSystems.newFileSystem(uri, Collections.emptyMap());
		} catch (Throwable throwable) {
			return null;
		}
	}

	public static Location getLocation(final Class<?> type) {
		final URL url = HRuntime.whereFrom(type);

		final URI uri;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		try (final FileSystem fileSystem = openFileSystem(uri)) {
			final Path target;
			{
				final Path packageDirectory = Paths.get(uri).toAbsolutePath().getParent();

				{ // Walk up the package names to the target directory
					final String[] packageNames = type.getPackage().getName().split("\\.+");
					final int nameCount = packageDirectory.getNameCount();
					if (nameCount < packageNames.length) throw new RuntimeException("Class was not in a proper package directory structure!");

					Path current = packageDirectory;
					for (int i = 0; i < packageNames.length; i++) {
						if (!current.getFileName().toString().equals(packageNames[packageNames.length - 1 - i])) throw new RuntimeException("Package name mismatch!");
						current = current.getParent();
					}
					target = current;
				}
			}

			try {
				final Layout layout = HStream.findOne(Stream.of(Layout.values()).filter(l -> l.isApplicableToTarget(target)));
				return layout.createFromTarget(target);
			} catch (Throwable throwable) {
				throw new RuntimeException(String.format("Did not recognize layout of class path \"%1$s\"!", target), throwable);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
