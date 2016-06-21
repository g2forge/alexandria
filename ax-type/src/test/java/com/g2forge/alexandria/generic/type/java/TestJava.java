package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.implementations.JavaClassType;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;

public class TestJava {
	public static class Child extends Parent<String> {}

	public static class O<T> {
		public class I<S> {
			public O<T>.I<S> field;
		}
	}

	public static class Parent<T> {
		public T field;
	}

	@Test
	public void testBind() {
		final IJavaClassType inner = new JavaClassType(O.I.class, EmptyTypeEnvironment.create());
		final ITypeEnvironment environment = inner.bind(CollectionHelpers.asList(new JavaClassType(String.class, EmptyTypeEnvironment.create()))).toEnvironment();
		final Type actual = inner.getParameters().get(0).eval(environment).getJavaType();
		Assert.assertEquals(String.class, actual);
	}

	@Test
	public void testBound() {
		final IJavaClassType inner = new JavaClassType(O.I.class, EmptyTypeEnvironment.create());
		final IJavaUntype type = inner.getFields(JavaScope.Instance, JavaProtection.Private).findAny().get().getType();
		Assert.assertTrue(type instanceof IJavaBoundType);
		final IJavaBoundType bound = (IJavaBoundType) type;
		Assert.assertEquals(inner, bound.getRaw());
		Assert.assertEquals(type, type.eval(bound.toEnvironment()));
	}

	@Test
	public void testChild() {
		final IJavaClassType child = new JavaClassType(Child.class, EmptyTypeEnvironment.create());
		final IJavaFieldType field = child.getFields(JavaScope.Inherited, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(String.class, field.getType().getJavaType());
	}

	@Test
	public void testResolved() {
		final IJavaClassType child = new JavaClassType(Child.class, EmptyTypeEnvironment.create());
		final IJavaFieldType field = child.getSuperClass().getFields(JavaScope.Instance, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(String.class, field.getType().getJavaType());
	}

	@Test
	public void testUnresolved() {
		final IJavaClassType child = new JavaClassType(Child.class, null);
		final IJavaFieldType field = child.getSuperClass().getFields(JavaScope.Instance, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(Parent.class.getTypeParameters()[0], field.getType().getJavaType());
	}
}
