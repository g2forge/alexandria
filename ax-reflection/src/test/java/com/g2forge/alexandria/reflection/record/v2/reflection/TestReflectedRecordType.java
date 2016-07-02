package com.g2forge.alexandria.reflection.record.v2.reflection;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;
import com.g2forge.alexandria.reflection.record.v2.IRecordType;
import com.g2forge.alexandria.reflection.record.v2.reflection.ReflectedRecordType;

public class TestReflectedRecordType {
	public static class FieldRecord {
		protected String foo;

		public FieldRecord(String foo) {
			this.foo = foo;
		}
	}

	public static class MethodRecord {
		public String getFoo() {
			return "bar";
		}
	}

	public static class OverrideRecord {
		protected String foo;

		public OverrideRecord(String foo) {
			this.foo = foo;
		}

		public String getFoo() {
			return "bar";
		}
	}

	@Test
	public void field() {
		final IRecordType recordType = new ReflectedRecordType(FieldRecord.class);
		final IPropertyType property = CollectionHelpers.getOne(recordType.getProperties());
		Assert.assertEquals("foo", property.getName());
		Assert.assertEquals("bar", property.getValue(new FieldRecord("bar")));

		{
			final FieldRecord object = new FieldRecord("abc");
			property.setValue(object, "bar");
			Assert.assertEquals("bar", object.foo);
		}
	}

	@Test
	public void method() {
		final IRecordType recordType = new ReflectedRecordType(MethodRecord.class);
		final IPropertyType property = CollectionHelpers.getOne(recordType.getProperties());
		Assert.assertEquals("foo", property.getName());
		Assert.assertEquals("bar", property.getValue(new MethodRecord()));
	}

	@Test
	public void override() {
		final IRecordType recordType = new ReflectedRecordType(OverrideRecord.class);
		final IPropertyType property = CollectionHelpers.getOne(recordType.getProperties());
		Assert.assertEquals("foo", property.getName());

		final OverrideRecord object = new OverrideRecord("abc");
		Assert.assertEquals("bar", property.getValue(object));
		Assert.assertEquals("abc", object.foo);
		try {
			property.setValue(object, "def");
			Assert.fail();
		} catch (UnsupportedOperationException exception) {}

		object.foo = "ghi";
		Assert.assertEquals("bar", property.getValue(object));
		Assert.assertEquals("ghi", object.foo);
	}
}
