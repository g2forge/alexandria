package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestCopyWalker {
	@Test
	public void copy() throws IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			Files.createDirectories(temp.get().resolve("x/a/b"));
			synchronized (this) {
				try {
					wait(1000);
				} catch (InterruptedException exception) {
					Assert.fail();
				}
			}
			HFile.copy(temp.get().resolve("x"), temp.get().resolve("y"), StandardCopyOption.COPY_ATTRIBUTES);
			Assert.assertEquals(HCollection.asList("a"), Files.list(temp.get().resolve("y")).map(Path::getFileName).map(Object::toString).collect(Collectors.toList()));
			Assert.assertEquals(Files.getLastModifiedTime(temp.get().resolve("x/a/b")), Files.getLastModifiedTime(temp.get().resolve("y/a/b")));
		}
	}
}
