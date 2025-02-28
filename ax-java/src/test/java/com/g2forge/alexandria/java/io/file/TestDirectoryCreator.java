package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.HTextIO;

import lombok.Getter;

public class TestDirectoryCreator {
	@Getter
	protected static TempDirectory temp = null;

	@AfterClass
	public static void after() {
		temp.close();
		temp = null;
	}

	@BeforeClass
	public static void before() {
		if (temp != null) throw new IllegalStateException();
		temp = new TempDirectory();
	}

	@Test
	public void contents() throws IOException {
		final String string = "Hello, World!";

		final Path root = temp.get().resolve("contents");
		DirectoryCreator.create(root, d0 -> {
			d0.dir("child", d1 -> {
				d1.file("file").from(HIO.toInputStream(string));
			});
		});
		Assert.assertTrue(Files.isDirectory(root));
		final Path file = root.resolve("child/file");
		Assert.assertTrue(Files.isRegularFile(file));
		Assert.assertEquals(string, HCollection.getOne(HTextIO.readAll(Files.newInputStream(file))));
	}

	@Test
	public void empty() throws IOException {
		final Path root = temp.get().resolve("empty");
		DirectoryCreator.create(root, d0 -> {
			d0.file("file").empty();
		});
		Assert.assertTrue(Files.isDirectory(root));
		final Path file = root.resolve("file");
		Assert.assertTrue(Files.isRegularFile(file));
		Assert.assertEquals(0l, Files.size(file));
	}

	@Test
	public void executable() throws IOException {
		final Path root = temp.get().resolve("executable");
		DirectoryCreator.create(root, d0 -> {
			d0.file("file").empty().executable();
		});
		Assert.assertTrue(Files.isDirectory(root));
		final Path file = root.resolve("file");
		Assert.assertTrue(Files.isRegularFile(file));
		Assert.assertEquals(0l, Files.size(file));
	}

	@Test
	public void nop() {
		final Path root = temp.get().resolve("nop");
		DirectoryCreator.create(root, null);
		Assert.assertTrue(Files.isDirectory(root));
	}

	@Test
	public void pathErrorAbsolute() {
		final Path root = temp.get().resolve("pathErrorAbsolute");
		try {
			DirectoryCreator.create(root, d0 -> {
				final Path absolute = Paths.get("").getFileSystem().getRootDirectories().iterator().next();
				d0.dir(absolute, null);
			});
			Assert.fail();
		} catch (IllegalArgumentException exception) {
			Assert.assertTrue(exception.getMessage().contains("absolute"));
		}

	}

	@Test
	public void pathErrorNotNested() {
		final Path root = temp.get().resolve("pathErrorAbsolute");
		try {
			DirectoryCreator.create(root, d0 -> {
				d0.dir("../foo", null);
			});
			Assert.fail();
		} catch (IllegalArgumentException exception) {
			// Assert.assertTrue(exception.getMessage().contains("outside"));
		}
	}

	@Test
	public void subdir() throws IOException {
		final Path root = temp.get().resolve("subdir");
		DirectoryCreator.create(root, d0 -> {
			d0.dir("child", null);
		});
		Assert.assertTrue(Files.isDirectory(root));
		Assert.assertTrue(Files.isDirectory(root.resolve("child")));
	}
}
