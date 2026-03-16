package com.g2forge.alexandria.parse.regex;

import org.junit.Test;

import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.parse.NamedCharacterClass;
import com.g2forge.alexandria.parse.QuanitifierVariant;
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

	@Test(timeout = 5000)
	public void catastrophicBacktrackingAvoidedWithPossessive() {
		// Build a pattern similar to one that would cause catastrophic backtracking without possessive quantifiers
		// Pattern: text("X") group( charClass(-, _, space).starPossessive() charClass(a-z, A-Z, 0-9).plusPossessive() ).plus().opt()
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("X").group(g -> {
			g.charClass(false, cc -> cc.character('-').character('_').named(NamedCharacterClass.Space)).star(QuanitifierVariant.POSSESSIVE);
			g.charClass(false, cc -> cc.range('a', 'z').range('A', 'Z').range('0', '9')).plus(QuanitifierVariant.POSSESSIVE);
		}).plus().opt().build();
		final long preTime = System.currentTimeMillis();
		// This should quickly return empty (no match) rather than hanging
		HAssert.assertTrue(pattern.match("X-abc-def-ghi.jkl-mno").isEmpty());
		// Make sure it finishes in 10ms or less.
		// This is very generous to ensure swapping or concurrency doesn't lead to spurious failures during testing.
		// Real backtracking would lead to an apparent hang, well well over 10ms.
		HAssert.assertTrue(System.currentTimeMillis() - preTime < 10);
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
	public void optPossessive() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").group(g -> g.text("b")).opt(QuanitifierVariant.POSSESSIVE).build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
	}

	@Test
	public void plus() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").plus().build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void plusPossessive() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").plus(QuanitifierVariant.POSSESSIVE).build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void repeatExact() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").repeat(1).build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertTrue(pattern.match("aa").isEmpty());
		HAssert.assertTrue(pattern.match("b").isEmpty());
	}

	@Test
	public void repeatMin() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").repeat(2, null).build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("aa").isEmpty());
		HAssert.assertFalse(pattern.match("aaa").isEmpty());
		HAssert.assertFalse(pattern.match("aaaa").isEmpty());
	}

	@Test
	public void repeatRange() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").repeat(2, 3).build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("aa").isEmpty());
		HAssert.assertFalse(pattern.match("aaa").isEmpty());
		HAssert.assertTrue(pattern.match("aaaa").isEmpty());
	}

	@Test
	public void star() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").star().build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void starPossessive() {
		final IMatcher<?, Regex> pattern = RegexMatcher.builder().text("a").text("b").star(QuanitifierVariant.POSSESSIVE).build();
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
