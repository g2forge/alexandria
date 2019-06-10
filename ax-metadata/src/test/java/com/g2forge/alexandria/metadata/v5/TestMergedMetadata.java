package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestMergedMetadata {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface A {
		public String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface B {
		public String value();
	}

	@A("A")
	public static class C {}

	@B("B")
	public static class D {}

	@Test
	public void test() {
		final IMetadata metadata = IMetadata.merge(IMetadata.of(C.class), IMetadata.of(D.class));
		HAssert.assertTrue(metadata.isMetadataPresent(A.class));
		HAssert.assertTrue(metadata.isMetadataPresent(B.class));
	}
}
