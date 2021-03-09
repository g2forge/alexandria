package com.g2forge.alexandria.parse.regex;

import org.junit.Test;

import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.parse.NamedCharacterClass;
import com.g2forge.alexandria.test.HAssert;

public class TestBasicRegexMatcher {
	@Test
	public void alt() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().alt(RegexMatcher.builder().text("a").build(), RegexMatcher.builder().text("b").build()).build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("b").isEmpty());
	}

	@Test
	public void caseInsensitive() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder(RegexMatcher.Flag.CASE_INSENSITIVE).text("a").build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("A").isEmpty());
	}

	@Test
	public void characterClass() {
		final IMatcher<?, Regex> charClass = RegexMatcher.builder().charClass(false, cc -> cc.range('0', '9')).build();
		final IMatcher<?, Regex> digit = RegexMatcher.builder().digit(10).build();
		for (int i = 0; i < 10; i++) {
			final String string = Character.toString((char) ('0' + i));
			HAssert.assertFalse(string, charClass.match(string).isEmpty());
			HAssert.assertFalse(string, digit.match(string).isEmpty());
		}
	}

	@Test
	public void groupNullArguments() {
		RegexMatcher.builder().group(g -> {});
	}

	@Test
	public void groupOptional() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").group(g -> g.text("b")).opt().build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
	}

	@Test
	public void groupRequired() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").group(g -> g.text("b")).build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
	}

	@Test
	public void match() {
		HAssert.assertFalse(RegexMatcher.builder().text("a").build().match("a").isEmpty());
	}

	@Test
	public void named() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().named(NamedCharacterClass.Space).build();
		HAssert.assertFalse(pattern.match(" ").isEmpty());
		HAssert.assertFalse(pattern.match("\t").isEmpty());
		HAssert.assertTrue(pattern.match("").isEmpty());
		HAssert.assertTrue(pattern.match("a").isEmpty());
	}

	@Test
	public void nonmatch() {
		HAssert.assertTrue(RegexMatcher.builder().text("a").build().match("").isEmpty());
	}

	@Test
	public void plus() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").plus().build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void star() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").star().build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void with() {
		final IMatcher<?, Regex> a = RegexMatcher.builder().text("a").build();
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("0").with(a).text("1").build();
		HAssert.assertFalse(pattern.match("0a1").isEmpty());
	}
}
