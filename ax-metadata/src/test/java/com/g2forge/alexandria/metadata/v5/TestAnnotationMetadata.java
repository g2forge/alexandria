package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestAnnotationMetadata {
	@Retained("Hello")
	@NotRetained("Hello")
	public static class Annotated {}

	public @interface NotRetained {
		public String value();
	}

	@Retained("Meta")
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Retained {
		public String value();
	}

	@Test
	public void meta() {
		final Retained a = IMetadata.of(Annotated.class).getMetadata(Retained.class);
		final Retained b = IMetadata.of(null, a).getMetadata(Retained.class);
		HAssert.assertEquals("Meta", b.value());
	}

	@Test(expected = IllegalArgumentException.class)
	public void notRetained() {
		IMetadata.of(Annotated.class).getMetadata(NotRetained.class);
	}

	@Test
	public void present() {
		HAssert.assertTrue(IMetadata.of(Annotated.class).isMetadataPresent(Retained.class));
		HAssert.assertFalse(IMetadata.of(TestAnnotationMetadata.class).isMetadataPresent(Retained.class));
	}

	@Test
	public void retained() {
		final Retained value = IMetadata.of(Annotated.class).getMetadata(Retained.class);
		HAssert.assertEquals("Hello", value.value());
	}
}
