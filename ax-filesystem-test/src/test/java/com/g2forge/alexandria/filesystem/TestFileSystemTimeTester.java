package com.g2forge.alexandria.filesystem;

import java.nio.file.Paths;

import org.junit.Test;

public class TestFileSystemTimeTester {
	@Test
	public void getCreationTimeMode() {
		// Make sure the method runs without an exception, we don't care what it returns
		FileSystemTimeTester.getCreationTimeMode(Paths.get("").toAbsolutePath());
	}

	@Test
	public void getLastAccessTimeMode() {
		// Make sure the method runs without an exception, we don't care what it returns
		FileSystemTimeTester.getLastAccessTimeMode(Paths.get("").toAbsolutePath());
	}
}
