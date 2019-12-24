package com.g2forge.alexandria.test;

import org.junit.ComparisonFailure;
import org.junit.Test;


public class TestHAssert {
	@Test
	public void equalsWithString() {
		HAssert.assertException(ComparisonFailure.class, "expected:<[A]> but was:<[B]>", () -> HAssert.assertEquals("", new Object(), new Object(), "A", "B"));
	}
}
