package com.g2forge.alexandria.path.path.filename;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class TestFilename {
	@Test
	public void extension0() {
		final Filename filename = Filename.fromString("file");
		Assert.assertEquals("file", filename.getName());
		Assert.assertEquals("file", filename.getPrefix().toString());
		Assert.assertNull(filename.getExtension());
		Assert.assertNull(filename.getSuffix());
		Assert.assertEquals("file", filename.toString());

	}

	@Test
	public void extension1() {
		final Filename filename = Filename.fromString("file.ext1");
		Assert.assertEquals("file", filename.getName());
		Assert.assertEquals("file", filename.getPrefix().toString());
		Assert.assertEquals("ext1", filename.getExtension());
		Assert.assertEquals("ext1", filename.getSuffix().toString());
		Assert.assertEquals("file.ext1", filename.toString());
	}

	@Test
	public void extension2() {
		final Filename filename = Filename.fromString("file.ext1.ext2");
		Assert.assertEquals("file", filename.getName());
		Assert.assertEquals("file.ext1", filename.getPrefix().toString());
		Assert.assertEquals("ext2", filename.getExtension());
		Assert.assertEquals("ext1.ext2", filename.getSuffix().toString());
		Assert.assertEquals("file.ext1.ext2", filename.toString());
	}

	@Test
	public void removeExtension() {
		Assert.assertEquals(Paths.get("A"), Filename.removeExtension(Paths.get("A.B"), "B"));
		Assert.assertEquals(Paths.get("A"), Filename.removeExtension(Paths.get("A.B.C"), "B.C"));
		Assert.assertEquals(Paths.get("A.B"), Filename.removeExtension(Paths.get("A.B.C"), "C"));
		try {
			Filename.removeExtension(Paths.get("A.B.C"), "D");
			Assert.fail();
		} catch (IllegalArgumentException exception) {}
	}
}
