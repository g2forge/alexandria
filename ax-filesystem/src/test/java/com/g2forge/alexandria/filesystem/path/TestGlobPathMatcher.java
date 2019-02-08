package com.g2forge.alexandria.filesystem.path;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.filesystem.path.GlobPathMatcher;

public class TestGlobPathMatcher extends ATestGeneric {
	@Test
	public void star() {
		final GlobPathMatcher matcher = new GlobPathMatcher("*.java", "/");
		Assert.assertTrue(matcher.matches(parse("foo.java")));
		Assert.assertFalse(matcher.matches(parse("foo/.java")));
	}

	@Test
	public void star2() {
		final GlobPathMatcher matcher = new GlobPathMatcher("**.java", "/");
		Assert.assertTrue(matcher.matches(parse("foo.java")));
		Assert.assertTrue(matcher.matches(parse("foo/.java")));
	}
}
