package com.g2forge.alexandria.java.core.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TestHRegex {
	@Test
	public void list() {
		final String regex = "(" + HRegex.toList("[a-z]+", ",\\s*") + ")(_foo)";
		final Matcher matcher = Pattern.compile(regex).matcher("one, two_foo");
		Assert.assertTrue(matcher.matches());
		Assert.assertEquals("one, two", matcher.group(1));
		Assert.assertEquals("_foo", matcher.group(2));
	}
}
