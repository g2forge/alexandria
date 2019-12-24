package com.g2forge.alexandria.java.text.quote;

import org.junit.Assert;
import org.junit.Test;

public class TestQuote {
	@Test
	public void alreadyQuoted() {
		Assert.assertEquals("'Hello, World!'", ShellQuoteType.BashDoubleExpand.quote(QuoteControl.IfNotAlready, "'Hello, World!'", ShellQuoteType.BashDoubleExpand, ShellQuoteType.BashSingle));
	}

	@Test
	public void escape() {
		Assert.assertEquals("\"\\\"\"", ShellQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "\""));
	}

	@Test
	public void needed() {
		Assert.assertEquals("\"Hello, World!\"", ShellQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "Hello, World!"));
	}

	@Test
	public void nonNeeded() {
		Assert.assertEquals("Hello", ShellQuoteType.BashDoubleExpand.quote(QuoteControl.IfNeeded, "Hello"));
	}
}
