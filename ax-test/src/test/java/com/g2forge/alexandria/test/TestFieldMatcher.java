package com.g2forge.alexandria.test;

import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TestFieldMatcher {
	@Data
	@AllArgsConstructor
	public static class Struct {
		protected String a;

		protected int b;
	}

	protected static class StructMatcher extends FieldMatcher<Struct> {
		protected static final FieldMatcher.FieldSet<Struct> FIELDS = new FieldMatcher.FieldSet<Struct>(Struct::getA, struct -> struct.b);

		public StructMatcher(Struct expected) {
			super(expected, FIELDS);
		}
	}

	@Test
	public void a() {
		final StringDescription description = new StringDescription();
		new StructMatcher(new Struct("x", 1)).describeMismatch(new Struct("y", 1), description);;
		Assert.assertEquals("a was \"y\" instead of \"x\"", description.toString());
	}

	@Test
	public void ab() {
		final StringDescription description = new StringDescription();
		new StructMatcher(new Struct("x", 1)).describeMismatch(new Struct("y", 2), description);;
		Assert.assertEquals("a was \"y\" instead of \"x\"\n" + FieldMatcher.INDENT + "b was <2> instead of <1>", description.toString());
	}

	@Test
	public void b() {
		final StringDescription description = new StringDescription();
		new StructMatcher(new Struct("a", -5)).describeMismatch(new Struct("a", 5), description);;
		Assert.assertEquals("b was <5> instead of <-5>", description.toString());
	}
}
