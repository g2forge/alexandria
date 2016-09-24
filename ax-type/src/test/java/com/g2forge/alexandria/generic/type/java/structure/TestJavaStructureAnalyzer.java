package com.g2forge.alexandria.generic.type.java.structure;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

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

	@Test
	public void inheritedNull() {
		Assert.assertEquals(HCollection.asList("d", "b", "c"), JavaStructureAnalyzer.REFLECTION_ANALYZER.getFields(Child.class, JavaScope.Inherited, null).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void inheritedProtected() {
		Assert.assertEquals(HCollection.asList("d", "b"), JavaStructureAnalyzer.REFLECTION_ANALYZER.getFields(Child.class, JavaScope.Inherited, JavaProtection.Protected).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void instancePrivate() {
		Assert.assertEquals(HCollection.asList("b", "c"), JavaStructureAnalyzer.REFLECTION_ANALYZER.getFields(Child.class, JavaScope.Instance, JavaProtection.Private).map(Field::getName).collect(Collectors.toList()));
	}

	@Test
	public void staticUnspecified() {
		Assert.assertEquals(HCollection.asList("a"), JavaStructureAnalyzer.REFLECTION_ANALYZER.getFields(Child.class, JavaScope.Static, JavaProtection.Unspecified).map(Field::getName).collect(Collectors.toList()));
	}
}
