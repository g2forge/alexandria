package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestMetadata {
	@Retained("Hello")
	@NotRetained("Hello")
	public static class Annotated {}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Retained {
		public String value();
	}

	public @interface NotRetained {
		public String value();
	}

	@Test
	public void retained() {
		final Retained annotation = HMetadata.access(Annotated.class).apply(Retained.class);
		HAssert.assertEquals("Hello", annotation.value());
	}

	@Test(expected = IllegalArgumentException.class)
	public void notRetained() {
		HMetadata.access(Annotated.class).apply(NotRetained.class);
	}
}
