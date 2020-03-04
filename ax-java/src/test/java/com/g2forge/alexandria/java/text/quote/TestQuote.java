package com.g2forge.alexandria.java.text.quote;

import org.junit.Assert;
import org.junit.Test;

public class TestQuote {
	@Test
	public void alreadyQuoted() {
		Assert.assertEquals("'Hello, World!'", BashQuoteType.BashDoubleExpand.quote(QuoteControl.IfNotAlready, "'Hello, World!'", BashQuoteType.BashDoubleExpand, BashQuoteType.BashSingle));
	}

	@Test
	public void escape() {
		Assert.assertEquals("\"\\\"\"", BashQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "\""));
	}

	@Test
	public void needed() {
		Assert.assertEquals("\"Hello, World!\"", BashQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "Hello, World!"));
	}

	@Test
	public void nonNeeded() {
		Assert.assertEquals("Hello", BashQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "Hello"));
	}
}
