package com.g2forge.alexandria.filesystem.memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class TestExample {
	@Test
	public void example0() throws IOException {
		try (final FileSystem fs = FileSystems.newFileSystem(URI.create("memory:mymemfilesystem"), null)) {
			final Path path = fs.getPath("/file.txt");
			final String message = "Hello, World!";
			Files.newBufferedWriter(path).append(message).append(System.lineSeparator()).close();
			try (final BufferedReader reader = Files.newBufferedReader(path)) {
				final String line = reader.readLine();
				Assert.assertEquals(message, line);
			}
		}
	}

	@Test
	public void example1() throws IOException {
		try (final FileSystem fs = FileSystems.newFileSystem(URI.create("memory:mymemfilesystem"), null)) {
			final Path path = Paths.get(URI.create("memory:mymemfilesystem!/directory"));
			Files.createDirectory(path);
			Files.delete(path);
		}
	}
}
