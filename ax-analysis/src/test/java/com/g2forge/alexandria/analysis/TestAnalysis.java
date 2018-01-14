package com.g2forge.alexandria.analysis;

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

	@Test
	public void method() throws NoSuchMethodException, SecurityException {
		final Method actual = SerializableFunction.analyze(TestClass1::getBar).getMethod();
		Assert.assertEquals(TestClass1.class.getDeclaredMethod("getBar"), actual);
	}

	@Test
	public void path() {
		final String actual = SerializableFunction.analyze((TestClass0 test) -> test.getFoo().getBar()).getPath();
		Assert.assertEquals("foo.bar", actual);
	}
}
