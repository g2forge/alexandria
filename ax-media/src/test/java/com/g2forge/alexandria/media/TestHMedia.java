package com.g2forge.alexandria.media;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestHMedia {
	@Test
	public void foo() {
		HAssert.assertNull(MediaType.getRegistry().computeMediaType("file.foo"));
	}

	@Test
	public void jpg() {
		HAssert.assertEquals(MediaType.JPG, MediaType.getRegistry().computeMediaType("file.jpg"));
		HAssert.assertEquals(MediaType.JPG, MediaType.getRegistry().computeMediaType("file.jpeg"));
	}

	@Test
	public void xml() {
		final IMediaType mediaType = MediaType.getRegistry().computeMediaType("file.xml");
		HAssert.assertEquals(MediaType.XML, mediaType);
		HAssert.assertTrue(mediaType.isText());
	}
}
