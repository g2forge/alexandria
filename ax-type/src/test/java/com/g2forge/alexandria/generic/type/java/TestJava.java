package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaClassType;
import com.g2forge.alexandria.java.core.helpers.HCollection;

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
		final ITypeEnvironment environment = inner.bind(HCollection.asList(new JavaClassType(String.class, EmptyTypeEnvironment.create()))).toEnvironment();
		final Type actual = inner.getParameters().get(0).eval(environment).getJavaType();
		Assert.assertEquals(String.class, actual);
	}

	@Test
	public void testBound() {
		final IJavaClassType inner = new JavaClassType(O.I.class, EmptyTypeEnvironment.create());
		final IJavaUntype type = inner.resolve().getFields(JavaScope.Instance, JavaProtection.Private).findAny().get().getFieldType();
		Assert.assertTrue(type instanceof IJavaConcreteType);
		final IJavaConcreteType bound = (IJavaConcreteType) type;
		Assert.assertEquals(inner, bound.getRaw());
		Assert.assertEquals(type, type.eval(bound.toEnvironment()));
		Assert.assertEquals(bound, bound.getFields(JavaScope.Instance, JavaProtection.Private).findAny().get().getFieldType());
	}

	@Test
	public void testChild() {
		final IJavaClassType child = new JavaClassType(Child.class, EmptyTypeEnvironment.create());
		final IJavaFieldType field = child.getFields(JavaScope.Inherited, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(String.class, field.getFieldType().getJavaType());
	}

	@Test
	public void testParent() {
		final IJavaClassType child = new JavaClassType(Child.class, EmptyTypeEnvironment.create());
		Assert.assertEquals(child.getSuperClass(), child.getParent(new JavaClassType(Parent.class, EmptyTypeEnvironment.create())));
	}

	@Test
	public void testResolved() {
		final IJavaClassType child = new JavaClassType(Child.class, EmptyTypeEnvironment.create());
		final IJavaFieldType field = child.getSuperClass().getFields(JavaScope.Instance, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(String.class, field.getFieldType().getJavaType());
	}

	@Test
	public void testUnresolved() {
		final IJavaClassType parent = new JavaClassType(Parent.class, null);
		final IJavaFieldType field = parent.getFields(JavaScope.Instance, JavaProtection.Private).findFirst().get();
		Assert.assertEquals(Parent.class.getTypeParameters()[0], field.getFieldType().getJavaType());
	}
}
