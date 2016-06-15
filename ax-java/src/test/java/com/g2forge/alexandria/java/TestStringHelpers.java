package com.g2forge.alexandria.java;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.StringHelpers;

public class TestStringHelpers {
	@Test
	public void lowercase() {
		Assert.assertEquals("word", StringHelpers.lowercase("Word"));
		Assert.assertEquals("tla", StringHelpers.lowercase("TLA"));
		Assert.assertEquals("xFoo", StringHelpers.lowercase("XFoo"));
		Assert.assertEquals("ab", StringHelpers.lowercase("AB"));
		Assert.assertEquals("abcFoo", StringHelpers.lowercase("ABCFoo"));
		Assert.assertEquals("ab_", StringHelpers.lowercase("AB_"));
		Assert.assertEquals("a_", StringHelpers.lowercase("A_"));
	}

	@Test
	public void propertyNames() {
		Assert.assertEquals("foo", StringHelpers.lowercase(StringHelpers.stripPrefix("getFoo", "get")));
		Assert.assertEquals("iFoo", StringHelpers.lowercase(StringHelpers.stripPrefix("getIFoo", "get")));
		Assert.assertEquals("a_Something", StringHelpers.lowercase(StringHelpers.stripPrefix("isA_Something", "is")));
		Assert.assertEquals("tla", StringHelpers.lowercase(StringHelpers.stripPrefix("isTLA", "is")));
		Assert.assertEquals("xfFoo", StringHelpers.lowercase(StringHelpers.stripPrefix("isXFFoo", "get", "is")));
	}

	@Test
	public void stripPrefix() {
		Assert.assertEquals("ab", StringHelpers.stripPrefix("ab", "0", "1"));
		Assert.assertEquals("b", StringHelpers.stripPrefix("ab", "a", "b"));
		Assert.assertEquals("ab", StringHelpers.stripPrefix("ab", "b"));
		Assert.assertEquals("b", StringHelpers.stripPrefix("ab", "b", "a"));
	}
}
