package com.g2forge.alexandria.java;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.reflection.JavaProtection;
import com.g2forge.alexandria.java.reflection.JavaScope;
import com.g2forge.alexandria.java.reflection.ReflectionHelpers;

public class TestReflectionHelpers {
	public static class Child extends Parent {
		static final String a = "a";

		public String b = "b";

		@SuppressWarnings("unused")
		private String c = "c";
	}

	public static class Parent {
		protected final String d = "d";
	}

	@Test
	public void inheritedNull() {
		Assert.assertEquals(Arrays.asList("d", "b", "c"), ReflectionHelpers.getFields(Child.class, JavaScope.Inherited, null).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void inheritedProtected() {
		Assert.assertEquals(Arrays.asList("d", "b"), ReflectionHelpers.getFields(Child.class, JavaScope.Inherited, JavaProtection.Protected).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void instancePrivate() {
		Assert.assertEquals(Arrays.asList("b", "c"), ReflectionHelpers.getFields(Child.class, JavaScope.Instance, JavaProtection.Private).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void staticUnspecified() {
		Assert.assertEquals(Arrays.asList("a"), ReflectionHelpers.getFields(Child.class, JavaScope.Static, JavaProtection.Unspecified).map(Field::getName).collect(Collectors.toList()));
	}
}
