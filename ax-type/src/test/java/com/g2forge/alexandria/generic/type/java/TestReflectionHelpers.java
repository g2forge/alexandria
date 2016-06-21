package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.type.java.JavaReflectionHelpers;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;

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
		Assert.assertEquals(CollectionHelpers.asList("d", "b", "c"), JavaReflectionHelpers.getFields(Child.class, JavaScope.Inherited, null).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void inheritedProtected() {
		Assert.assertEquals(CollectionHelpers.asList("d", "b"), JavaReflectionHelpers.getFields(Child.class, JavaScope.Inherited, JavaProtection.Protected).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void instancePrivate() {
		Assert.assertEquals(CollectionHelpers.asList("b", "c"), JavaReflectionHelpers.getFields(Child.class, JavaScope.Instance, JavaProtection.Private).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void staticUnspecified() {
		Assert.assertEquals(CollectionHelpers.asList("a"), JavaReflectionHelpers.getFields(Child.class, JavaScope.Static, JavaProtection.Unspecified).map(Field::getName).collect(Collectors.toList()));
	}
}
