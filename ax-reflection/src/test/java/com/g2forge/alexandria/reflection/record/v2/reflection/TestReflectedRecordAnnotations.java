package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;
import com.g2forge.alexandria.reflection.record.v2.IRecordType;
import com.g2forge.alexandria.reflection.record.v2.reflection.ReflectedRecordType;

import lombok.Data;

public class TestReflectedRecordAnnotations {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface TestAnnotation {}

	@Data
	public static class FieldRecord {
		@TestAnnotation
		protected String foo;
	}

	@Test
	public void annotation() {
		final IRecordType recordType = new ReflectedRecordType(FieldRecord.class);
		final IPropertyType<?> property = CollectionHelpers.getOne(recordType.getProperties());
		Assert.assertEquals("foo", property.getName());
		Assert.assertTrue(property instanceof MethodPropertyType);
		Assert.assertTrue(property.getAnnotations().isAnnotated(TestAnnotation.class));
	}
}
