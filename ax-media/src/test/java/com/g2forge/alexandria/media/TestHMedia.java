package com.g2forge.alexandria.media;

import org.junit.Test;

import com.g2forge.alexandria.java.io.Filename;
import com.g2forge.alexandria.test.HAssert;

public class TestHMedia {
	@Test
	public void foo() {
		HAssert.assertNull(MediaType.getRegistry().computeMediaType(new Filename("file.foo")));
	}

	@Test
	public void jpg() {
		HAssert.assertEquals(MediaType.JPG, MediaType.getRegistry().computeMediaType(new Filename("file.jpg")));
		HAssert.assertEquals(MediaType.JPG, MediaType.getRegistry().computeMediaType(new Filename("file.jpeg")));
	}

	@Test
	public void xml() {
		final IMediaType mediaType = MediaType.getRegistry().computeMediaType(new Filename("file.xml"));
		HAssert.assertEquals(MediaType.XML, mediaType);
		HAssert.assertTrue(mediaType.isText());
	}
}
