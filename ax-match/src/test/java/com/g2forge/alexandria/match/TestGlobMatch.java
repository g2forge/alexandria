package com.g2forge.alexandria.match;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestGlobMatch {
	@Test
	public void isNull() {
		HAssert.assertFalse(new GlobMatch(GlobMatch.PREFIX_GLOB + "*").test(null));
	}

	@Test
	public void anything() {
		HAssert.assertTrue(new GlobMatch(GlobMatch.PREFIX_GLOB + "*").test("hello"));
		HAssert.assertTrue(new GlobMatch(GlobMatch.PREFIX_GLOB + "x*x").test("xhellox"));
		HAssert.assertFalse(new GlobMatch(GlobMatch.PREFIX_GLOB + "x*x").test("hello"));
	}

	@Test
	public void one() {
		HAssert.assertTrue(new GlobMatch(GlobMatch.PREFIX_GLOB + "A?B").test("AxB"));
		HAssert.assertFalse(new GlobMatch(GlobMatch.PREFIX_GLOB + "A?B").test("AB"));
	}
}
