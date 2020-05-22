package com.g2forge.alexandria.java.io.file;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.io.Filename;

public class TestFilename {
	@Test
	public void extension0() {
		final Filename filename = new Filename("file");
		Assert.assertEquals("file", filename.getFirstName());
		Assert.assertEquals("file", filename.getFullName());
		Assert.assertNull(filename.getLastExtension());
		Assert.assertNull(filename.getFullExtensions());
		Assert.assertEquals("file", filename.toString());

	}

	@Test
	public void extension1() {
		final Filename filename = new Filename("file.ext1");
		Assert.assertEquals("file", filename.getFirstName());
		Assert.assertEquals("file", filename.getFullName());
		Assert.assertEquals("ext1", filename.getLastExtension());
		Assert.assertEquals("ext1", filename.getFullExtensions());
		Assert.assertEquals("file.ext1", filename.toString());
	}

	@Test
	public void extension2() {
		final Filename filename = new Filename("file.ext1.ext2");
		Assert.assertEquals("file", filename.getFirstName());
		Assert.assertEquals("file.ext1", filename.getFullName());
		Assert.assertEquals("ext2", filename.getLastExtension());
		Assert.assertEquals("ext1.ext2", filename.getFullExtensions());
		Assert.assertEquals("file.ext1.ext2", filename.toString());
	}
}
