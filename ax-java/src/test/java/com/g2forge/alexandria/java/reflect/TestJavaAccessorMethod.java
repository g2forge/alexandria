package com.g2forge.alexandria.java.reflect;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;

public class TestJavaAccessorMethod {
	@Data
	public static class TestData {
		protected final String string;

		protected final boolean flag;
	}

	@Test
	public void flag() throws NoSuchMethodException, SecurityException {
		final JavaAccessorMethod method = new JavaAccessorMethod(TestData.class.getDeclaredMethod("isFlag"));
		Assert.assertEquals(IJavaAccessorMethod.Accessor.IS, method.getAccessorType());
	}

	@Test
	public void string() throws NoSuchMethodException, SecurityException {
		final JavaAccessorMethod method = new JavaAccessorMethod(TestData.class.getDeclaredMethod("getString"));
		Assert.assertEquals(IJavaAccessorMethod.Accessor.GET, method.getAccessorType());
	}
}
