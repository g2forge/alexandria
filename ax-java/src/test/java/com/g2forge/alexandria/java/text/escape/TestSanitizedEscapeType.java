package com.g2forge.alexandria.java.text.escape;

import org.junit.Assert;
import org.junit.Test;

public class TestSanitizedEscapeType {
	protected static final SanitizedEscapeType sanitizedEscapeType = new SanitizedEscapeType("/:");

	@Test
	public void escape() {
		final String escaped = "a%2fb%3ac";
		final String unescaped = "a/b:c";
		Assert.assertEquals(escaped, sanitizedEscapeType.getEscaper().escape(unescaped));
		Assert.assertEquals(unescaped, sanitizedEscapeType.getEscaper().unescape(escaped));
	}

	@Test
	public void noEscape() {
		final String value = "hello";
		Assert.assertEquals(value, sanitizedEscapeType.getEscaper().escape(value));
		Assert.assertEquals(value, sanitizedEscapeType.getEscaper().unescape(value));
	}
}
