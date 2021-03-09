package com.g2forge.alexandria.java.text;

import org.junit.Assert;
import org.junit.Test;

public class TestHString {
	@Test
	public void initialsAcronym() {
		Assert.assertEquals("A", HString.initials("ABC"));
	}

	@Test
	public void initialsLowercaseWords() {
		Assert.assertEquals("AHW", HString.initials("ABC Hello from the World"));
	}

	@Test
	public void initialsWords() {
		Assert.assertEquals("AHW", HString.initials("ABC Hello World"));
	}

	@Test
	public void lowercase() {
		Assert.assertEquals("word", HString.lowercase("Word"));
		Assert.assertEquals("tla", HString.lowercase("TLA"));
		Assert.assertEquals("xFoo", HString.lowercase("XFoo"));
		Assert.assertEquals("ab", HString.lowercase("AB"));
		Assert.assertEquals("abcFoo", HString.lowercase("ABCFoo"));
		Assert.assertEquals("ab_", HString.lowercase("AB_"));
		Assert.assertEquals("a_", HString.lowercase("A_"));
	}

	@Test
	public void match0() {
		Assert.assertEquals(0, HString.nMatchesBefore("x1", 1, 'y'));
	}

	@Test
	public void match1() {
		Assert.assertEquals(1, HString.nMatchesBefore("x1foobar", 1, 'x'));
	}

	@Test
	public void match2() {
		Assert.assertEquals(2, HString.nMatchesBefore("foobarxx1foobar", 8, 'x'));
	}

	@Test
	public void matchEmpty() {
		Assert.assertEquals(0, HString.nMatchesBefore("", 0, 'x'));
	}

	@Test
	public void propertyNames() {
		Assert.assertEquals("foo", HString.lowercase(HString.stripPrefix("getFoo", "get")));
		Assert.assertEquals("iFoo", HString.lowercase(HString.stripPrefix("getIFoo", "get")));
		Assert.assertEquals("a_Something", HString.lowercase(HString.stripPrefix("isA_Something", "is")));
		Assert.assertEquals("tla", HString.lowercase(HString.stripPrefix("isTLA", "is")));
		Assert.assertEquals("xfFoo", HString.lowercase(HString.stripPrefix("isXFFoo", "get", "is")));
	}

	@Test
	public void stripPrefix() {
		Assert.assertEquals("ab", HString.stripPrefix("ab", "0", "1"));
		Assert.assertEquals("b", HString.stripPrefix("ab", "a", "b"));
		Assert.assertEquals("ab", HString.stripPrefix("ab", "b"));
		Assert.assertEquals("b", HString.stripPrefix("ab", "b", "a"));
	}
}
