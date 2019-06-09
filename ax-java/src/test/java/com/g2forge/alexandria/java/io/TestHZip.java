package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.g2forge.alexandria.java.io.file.HZip;
import com.g2forge.alexandria.java.io.file.TempDirectory;

public class TestHZip {
	@Test
	public void copy() throws IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			final List<Path> paths = IntStream.range(0, 3).mapToObj(i -> temp.getPath().resolve(i + ".zip")).collect(Collectors.toList());
			for (Path path : paths) {
				temp.getResource().resource(getClass(), "zipequals.zip", path);
			}
			HZip.isEqual(paths);
		}
	}
}
