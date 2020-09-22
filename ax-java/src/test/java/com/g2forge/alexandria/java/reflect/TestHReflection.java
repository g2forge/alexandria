package com.g2forge.alexandria.java.reflect;

import org.junit.Assert;
import org.junit.Test;

public class TestHReflection {
	public static interface SignatureMethod {
		public Void method(String... strings);
	}

	@Test
	public void signatureArray() {
		Assert.assertEquals("[Ljava/lang/String;", HReflection.toSignature(String[].class));
	}

	@Test
	public void signatureMethod() {
		Assert.assertEquals("([Ljava/lang/String;)Ljava/lang/Void;", HReflection.toSignature(SignatureMethod.class.getDeclaredMethods()[0]));
	}

	@Test
	public void signatureVoid() {
		Assert.assertEquals("V", HReflection.toSignature(Void.TYPE));
	}
}
