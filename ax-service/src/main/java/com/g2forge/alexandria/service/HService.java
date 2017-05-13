package com.g2forge.alexandria.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.g2forge.alexandria.java.core.helpers.HRuntime;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.Data;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HService {
	@Data
	public static class SourceDirectory {
		protected final Path source;

		protected final Path resources;

		protected final Path target;
	}

	public enum Layout {
		MavenMain {
			@Override
			public SourceDirectory createFromTarget(Path path) {
				final int nameCount = path.getNameCount();
				if (nameCount < 2) return null;
				if (!path.getName(nameCount - 1).toString().equals("classes")) return null;
				if (!path.getName(nameCount - 2).toString().equals("target")) return null;
				final Path main = path.resolve("../../src/main");
				return new SourceDirectory(main.resolve("java"), main.resolve("resources"), path);
			}
		},
		MavenTest {
			@Override
			public SourceDirectory createFromTarget(Path path) {
				final int nameCount = path.getNameCount();
				if (nameCount < 2) return null;
				if (!path.getName(nameCount - 1).toString().equals("test-classes")) return null;
				if (!path.getName(nameCount - 2).toString().equals("target")) return null;
				final Path main = path.resolve("../../src/test");
				return new SourceDirectory(main.resolve("java"), main.resolve("resources"), path);
			}
		};

		public abstract SourceDirectory createFromTarget(Path path);
	}

	public static <S> void writeSPIFile(Class<S> service, Class<? extends S> implementation) {
		final URL url = HRuntime.whereFrom(implementation);

		final URI uri;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		final Path path = Paths.get(uri).toAbsolutePath();
		final String[] packageNames = implementation.getPackage().getName().split("\\.+");
		final Path packageDirectory = path.getParent();

		final int nameCount = packageDirectory.getNameCount();
		if (nameCount < packageNames.length) throw new RuntimeException("Class was not in a proper package directory structure!");
		Path current = packageDirectory;
		for (int i = 0; i < packageNames.length; i++) {
			if (!current.getFileName().toString().equals(packageNames[packageNames.length - 1 - i])) throw new RuntimeException("Package name mismatch!");
			current = current.getParent();
		}

		SourceDirectory sourceDirectory = null;
		for (Layout layout : Layout.values()) {
			sourceDirectory = layout.createFromTarget(current);
			if (sourceDirectory != null) break;
		}
		if (sourceDirectory == null) throw new RuntimeException("Did not recognize layout of class path!");

		try {
			final Path directory = sourceDirectory.getResources().resolve("META-INF/services");
			Files.createDirectories(directory);
			final Path file = directory.resolve(service.getName());
			try (final BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
				writer.append(String.format("%1$s%n", implementation.getName()));
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
