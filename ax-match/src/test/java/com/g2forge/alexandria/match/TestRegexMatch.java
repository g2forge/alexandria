package com.g2forge.alexandria.match;

import java.util.regex.Pattern;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestRegexMatch {
	@Test
	public void isNull() {
		HAssert.assertFalse(new RegexMatch(RegexMatch.PREFIX_REGEX + ".*").test(null));
	}

	@Test
	public void quoted() {
		final String hello = "hello";
		HAssert.assertTrue(new RegexMatch(RegexMatch.PREFIX_REGEX + Pattern.quote(hello)).test(hello));
		HAssert.assertFalse(new RegexMatch(RegexMatch.PREFIX_REGEX + Pattern.quote(hello)).test(hello + ", world!"));
	}
}
