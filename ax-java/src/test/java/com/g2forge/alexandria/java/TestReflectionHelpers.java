package com.g2forge.alexandria.java;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.java.reflection.JavaClass;
import com.g2forge.alexandria.java.reflection.JavaProtection;
import com.g2forge.alexandria.java.reflection.JavaScope;

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

	protected static final JavaClass<Child> reflection = new JavaClass<>(Child.class);

	@Test
	public void inheritedNull() {
		Assert.assertEquals(CollectionHelpers.asList("d", "b", "c"), reflection.getFields(JavaScope.Inherited, null).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void inheritedProtected() {
		Assert.assertEquals(CollectionHelpers.asList("d", "b"), reflection.getFields(JavaScope.Inherited, JavaProtection.Protected).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void instancePrivate() {
		Assert.assertEquals(CollectionHelpers.asList("b", "c"), reflection.getFields(JavaScope.Instance, JavaProtection.Private).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void staticUnspecified() {
		Assert.assertEquals(CollectionHelpers.asList("a"), reflection.getFields(JavaScope.Static, JavaProtection.Unspecified).map(Field::getName).collect(Collectors.toList()));
	}
}
