package com.g2forge.alexandria.generic.type.java.structure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.java.function.FunctionHelpers;

public class TestJavaStructureAnalyzer {
	public static class Child extends Parent {
		static final String a = "a";

		public String b = "b";

		@SuppressWarnings("unused")
		private String c = "c";
	}

	public static class Parent {
		protected final String d = "d";
	}

	protected final JavaStructureAnalyzer<Class<?>, Field, Method> analyzer = new JavaStructureAnalyzer<Class<?>, Field, Method>(klass -> Object.class.equals(klass), Class::getSuperclass, FunctionHelpers.compose(Class::getDeclaredMethods, Stream::of), Function.identity(), FunctionHelpers.compose(Class::getDeclaredFields, Stream::of), Function.identity());

	@Test
	public void inheritedNull() {
		Assert.assertEquals(CollectionHelpers.asList("d", "b", "c"), analyzer.getFields(Child.class, JavaScope.Inherited, null).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void inheritedProtected() {
		Assert.assertEquals(CollectionHelpers.asList("d", "b"), analyzer.getFields(Child.class, JavaScope.Inherited, JavaProtection.Protected).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void instancePrivate() {
		Assert.assertEquals(CollectionHelpers.asList("b", "c"), analyzer.getFields(Child.class, JavaScope.Instance, JavaProtection.Private).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void staticUnspecified() {
		Assert.assertEquals(CollectionHelpers.asList("a"), analyzer.getFields(Child.class, JavaScope.Static, JavaProtection.Unspecified).map(Field::getName).collect(Collectors.toList()));
	}
}
