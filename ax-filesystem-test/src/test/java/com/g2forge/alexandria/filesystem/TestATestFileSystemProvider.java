package com.g2forge.alexandria.filesystem;

import java.nio.file.Paths;

import org.junit.Test;

public class TestATestFileSystemProvider {
	@Test
	public void isSupportsLastAccess() {
		// Make sure the method runs without an exception, we don't care what it returns
		ATestFileSystemProvider.isSupportsLastAccess(Paths.get("").toAbsolutePath());
	}
}
