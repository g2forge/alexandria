package com.g2forge.alexandria.java.core.enums;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.IFunction1;

public class TestHEnum {
	public static enum E {
		A,
		b,
		C {
			@Override
			public String toString() {
				return "X";
			}
		};
	}

	@Test
	public void valueOf() {
		Assert.assertEquals(E.A, E.valueOf("A"));
		try {
			E.valueOf("B");
			Assert.fail("Should have thrown an exception!");
		} catch (IllegalArgumentException e) {}
		Assert.assertEquals(E.C, E.valueOf("C"));
	}

	@Test
	public void valueOfCaseInsensitive() {
		Assert.assertEquals(E.A, HEnum.valueOfInsensitive(E.class, "A"));
		Assert.assertEquals(E.b, HEnum.valueOfInsensitive(E.class, "B"));
		Assert.assertEquals(E.C, HEnum.valueOfInsensitive(E.class, "C"));
	}

	@Test
	public void valueOfString() {
		Assert.assertEquals(E.A, HEnum.valueOf(E.class, Object::toString, IFunction1.Identity.create(), "A"));
		try {
			HEnum.valueOf(E.class, Object::toString, IFunction1.Identity.create(), "B");
			Assert.fail("Should have thrown an exception!");
		} catch (IllegalArgumentException e) {}
		Assert.assertEquals(E.C, HEnum.valueOf(E.class, Object::toString, IFunction1.Identity.create(), "X"));
	}
}
