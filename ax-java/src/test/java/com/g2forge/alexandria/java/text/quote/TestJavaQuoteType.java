package com.g2forge.alexandria.java.text.quote;

import org.junit.Assert;
import org.junit.Test;

public class TestJavaQuoteType {
	@Test
	public void patternCharacterClass() {
		Assert.assertEquals("[abc]", JavaQuoteType.PatternCharClass.quote(QuoteControl.IfNeeded, "abc"));
	}

	@Test
	public void stringQuoting() {
		Assert.assertEquals("\"Hello, World!\\n\"", JavaQuoteType.String.quote(QuoteControl.IfNeeded, "Hello, World!\n", JavaQuoteType.String));
	}
}
