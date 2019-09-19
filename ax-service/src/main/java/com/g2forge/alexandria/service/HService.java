package com.g2forge.alexandria.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.project.HProject;
import com.g2forge.alexandria.java.project.Location;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HService {
	@SafeVarargs
	public static <S> void writeSPIFile(Class<S> service, Class<? extends S>... implementations) {
		writeSPIFile(service, service, HCollection.asList(implementations));
	}

	public static <S> void writeSPIFile(Class<?> key, Class<S> type, Collection<? extends Class<? extends S>> implementations) {
		final Map<Location, List<Class<? extends S>>> grouped = implementations.stream().collect(Collectors.groupingBy(HProject::getLocation));
		for (Map.Entry<Location, List<Class<? extends S>>> entry : grouped.entrySet()) {
			try {
				final Path directory = entry.getKey().getResources().resolve("META-INF/services");
				Files.createDirectories(directory);
				final Path file = directory.resolve(key.getName());
				try (final BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
					for (Class<? extends S> implementation : entry.getValue()) {
						writer.append(String.format("%1$s%n", implementation.getName()));
					}
				}
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		}
	}
}
