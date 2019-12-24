package com.g2forge.alexandria.java.reflect.accessor;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.reflect.accessor.JavaAccessorMethod;
import com.g2forge.alexandria.java.reflect.accessor.JavaAccessorType;

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
		Assert.assertEquals(JavaAccessorType.IS, method.getAccessorType());
	}

	@Test
	public void string() throws NoSuchMethodException, SecurityException {
		final JavaAccessorMethod method = new JavaAccessorMethod(TestData.class.getDeclaredMethod("getString"));
		Assert.assertEquals(JavaAccessorType.GET, method.getAccessorType());
	}
}
