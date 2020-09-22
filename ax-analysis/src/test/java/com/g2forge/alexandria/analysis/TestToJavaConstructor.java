package com.g2forge.alexandria.analysis;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.type.ref.ITypeRef;

public class TestToJavaConstructor {
	public static class Cons {
		public Cons(String string) {}
	}

	@Test
	public void constructor() {
		final IMethodAnalyzer methodAnalyzer = ISerializableFunction1.create(Cons::new).asMethodAnalyzer();
		Assert.assertNotNull(methodAnalyzer.getConstructor());
		Assert.assertEquals(ITypeRef.of(Cons.class), methodAnalyzer.getReturnType());
	}
}
