package com.g2forge.alexandria.analysis;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;

public class TestAnalysis {
	@Data
	public static class TestClass0 {
		protected final TestClass1 foo;
	}

	@Data
	public static class TestClass1 {
		protected final Object bar;
	}

	public static interface TestClass2 {
		public Object bar();
	}

	@Test
	public void method() throws NoSuchMethodException, SecurityException {
		final Method actual = ISerializableFunction1.analyze(TestClass1::getBar).getMethod();
		Assert.assertEquals(TestClass1.class.getDeclaredMethod("getBar"), actual);
	}

	@Test
	public void nonSerializable() {
		ObjectStreamClass.lookup(TestClass1.class);
		Assert.assertEquals("bar", ISerializableSupplier.create(new TestClass1(null)::getBar).asMethodAnalyzer().getPath());
	}

	@Test
	public void path() {
		final String actual = ISerializableFunction1.analyze((TestClass0 test) -> test.getFoo().getBar()).getPath();
		Assert.assertEquals("foo.bar", actual);
	}

	@Test
	public void pathNoPrefix() {
		final String actual = ISerializableFunction1.analyze((TestClass2 test) -> test.bar()).getPath();
		Assert.assertEquals("bar", actual);
	}
}
