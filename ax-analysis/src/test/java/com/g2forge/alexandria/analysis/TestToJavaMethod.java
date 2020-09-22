package com.g2forge.alexandria.analysis;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.type.ref.ITypeRef;

public class TestToJavaMethod {
	public static interface IArray {
		public Object[] method();
	}

	public static interface ILong {
		public long method();
	}

	public static interface IObject {
		public Object method();
	}

	@Test
	public void _long() {
		final IMethodAnalyzer methodAnalyzer = ISerializableFunction1.create(ILong::method).asMethodAnalyzer();
		Assert.assertNotNull(methodAnalyzer.getMethod());
		Assert.assertEquals(ITypeRef.of(Long.TYPE), methodAnalyzer.getReturnType());
	}

	@Test
	public void array() {
		final IMethodAnalyzer methodAnalyzer = ISerializableFunction1.create(IArray::method).asMethodAnalyzer();
		Assert.assertNotNull(methodAnalyzer.getMethod());
		Assert.assertEquals(ITypeRef.of(Object[].class), methodAnalyzer.getReturnType());
	}

	@Test
	public void object() {
		final IMethodAnalyzer methodAnalyzer = ISerializableFunction1.create(IObject::method).asMethodAnalyzer();
		Assert.assertNotNull(methodAnalyzer.getMethod());
		Assert.assertEquals(ITypeRef.of(Object.class), methodAnalyzer.getReturnType());
	}
}
