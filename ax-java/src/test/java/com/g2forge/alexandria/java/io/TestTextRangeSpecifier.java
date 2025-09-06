package com.g2forge.alexandria.java.io;

import org.junit.Assert;
import org.junit.Test;

public class TestTextRangeSpecifier {
	@Test(expected = IllegalArgumentException.class)
	public void badCharacterRange() {
		new TextRangeSpecifier(0, 0, 4, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void badLine() {
		new TextRangeSpecifier(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void badLineRange() {
		new TextRangeSpecifier(5, 4);
	}

	@Test
	public void multiCharacter() {
		final TextRangeSpecifier actual = new TextRangeSpecifier(3, 4, 10, 2);
		Assert.assertEquals("3:10 to 4:2", actual.toString());
	}

	@Test
	public void multiLine() {
		final TextRangeSpecifier actual = new TextRangeSpecifier(1, 5);
		Assert.assertEquals(new TextRangeSpecifier(1, 5, 0, 0), actual);
		Assert.assertEquals("1 to 5", actual.toString());
	}

	@Test
	public void singleCharacter() {
		final TextRangeSpecifier actual = new TextRangeSpecifier(3, 3, 5, 10);
		Assert.assertEquals("3:5 to 3:10", actual.toString());
	}

	@Test
	public void singleLine() {
		final TextRangeSpecifier actual = new TextRangeSpecifier(0);
		Assert.assertEquals(new TextRangeSpecifier(0, 1, 0, 0), actual);
		Assert.assertEquals("0", actual.toString());
	}
}
