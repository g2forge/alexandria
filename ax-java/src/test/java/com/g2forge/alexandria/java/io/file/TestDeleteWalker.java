package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestDeleteWalker {
	@Test
	public void delete() throws IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			Files.createDirectories(temp.get().resolve("x/a/b"));
			HFile.delete(temp.get().resolve("x"), true);
			Assert.assertTrue(Files.list(temp.get()).collect(Collectors.toList()).isEmpty());
		}
	}

	@Test
	public void keep() throws IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			Files.createDirectories(temp.get().resolve("a/b/keep"));
			Files.createDirectories(temp.get().resolve("c"));
			DeleteWalker.builder().onexit(false).keep(path -> path.getFileName().toString().equals("keep")).build().walkFileTree(temp.get());
			Assert.assertEquals(HCollection.asList("a"), Files.list(temp.get()).map(Path::getFileName).map(Object::toString).collect(Collectors.toList()));
		}
	}
}
