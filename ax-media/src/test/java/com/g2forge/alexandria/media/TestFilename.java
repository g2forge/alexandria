package com.g2forge.alexandria.media;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestFilename {
	@Test
	public void extension0() {
		final Filename filename = new Filename("file");
		HAssert.assertEquals("file", filename.getFirstName());
		HAssert.assertEquals("file", filename.getFullName());
		HAssert.assertNull(filename.getLastExtension());
		HAssert.assertEquals("file", filename.toString());

	}

	@Test
	public void extension1() {
		final Filename filename = new Filename("file.ext1");
		HAssert.assertEquals("file", filename.getFirstName());
		HAssert.assertEquals("file", filename.getFullName());
		HAssert.assertEquals("ext1", filename.getLastExtension());
		HAssert.assertEquals("file.ext1", filename.toString());
	}

	@Test
	public void extension2() {
		final Filename filename = new Filename("file.ext1.ext2");
		HAssert.assertEquals("file", filename.getFirstName());
		HAssert.assertEquals("file.ext1", filename.getFullName());
		HAssert.assertEquals("ext2", filename.getLastExtension());
		HAssert.assertEquals("file.ext1.ext2", filename.toString());
	}
}
