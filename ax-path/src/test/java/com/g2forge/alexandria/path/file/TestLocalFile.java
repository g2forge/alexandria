package com.g2forge.alexandria.path.file;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

import lombok.Getter;

public class TestLocalFile {
	@Getter(lazy = true)
	private final Path directory = Paths.get(System.getProperty("user.home"));

	@Test
	public void get() {
		final Path directory = getDirectory();
		HAssert.assertEquals(new LocalFile(directory), new LocalFile(directory).get("."));
	}

	@Test
	public void getParent() {
		final Path directory = getDirectory();
		HAssert.assertEquals(new LocalFile(directory.getParent()), new LocalFile(directory).getParent());
	}

	@Test
	public void isDirectory() {
		final Path directory = getDirectory();
		HAssert.assertTrue(new LocalFile(directory).isDirectory());
	}
}
