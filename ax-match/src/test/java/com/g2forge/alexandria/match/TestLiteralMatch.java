package com.g2forge.alexandria.match;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestLiteralMatch {
	@Test
	public void isNull() {
		HAssert.assertTrue(new LiteralMatch(null).test(null));
	}

	@Test
	public void mismatch() {
		HAssert.assertFalse(new LiteralMatch("A").test("B"));
	}

	@Test
	public void string() {
		final String string = "string";
		HAssert.assertTrue(new LiteralMatch(string).test(string));
	}
}
