package com.g2forge.alexandria.analysis;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertNotNull(ISerializableFunction1.create(ILong::method).asMethodAnalyzer().getMethod());
	}

	@Test
	public void array() {
		Assert.assertNotNull(ISerializableFunction1.create(IArray::method).asMethodAnalyzer().getMethod());
	}

	@Test
	public void object() {
		Assert.assertNotNull(ISerializableFunction1.create(IObject::method).asMethodAnalyzer().getMethod());
	}
}
