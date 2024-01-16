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
	public void getEnumClass() {
		Assert.assertEquals(E.class, HEnum.getEnumClass(E.A));
		Assert.assertEquals(E.class, HEnum.getEnumClass(E.C));
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
		Assert.assertEquals(E.A, HEnum.valueOf(E.class, Object::toString, true, IFunction1.Identity.create(), "A"));
		try {
			HEnum.valueOf(E.class, Object::toString, true, IFunction1.Identity.create(), "B");
			Assert.fail("Should have thrown an exception!");
		} catch (IllegalArgumentException e) {}
		Assert.assertEquals(E.C, HEnum.valueOf(E.class, Object::toString, true, IFunction1.Identity.create(), "X"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void minEmpty() {
		HEnum.min();
	}

	@Test(expected = IllegalArgumentException.class)
	public void maxEmpty() {
		HEnum.max();
	}

	@Test
	public void max1() {
		Assert.assertEquals(E.A, HEnum.max(E.A));
	}

	@Test
	public void min1() {
		Assert.assertEquals(E.A, HEnum.min(E.A));
	}

	@Test
	public void max2() {
		Assert.assertEquals(E.b, HEnum.max(E.A, E.b));
		Assert.assertEquals(E.C, HEnum.max(E.C, E.b));
	}

	@Test
	public void min2() {
		Assert.assertEquals(E.A, HEnum.min(E.A, E.b));
		Assert.assertEquals(E.b, HEnum.min(E.C, E.b));
	}
}
