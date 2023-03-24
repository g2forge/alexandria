package com.g2forge.alexandria.java.text.casing;

import org.junit.Assert;
import org.junit.Test;

public class TestCase {
	public static void assertConversion(String expected, ICase target, ICase source, String input) {
		final String actual = target.toString(source.fromString(input));
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void camel() {
		final ICase cc = CamelCase.create();
		final String string = "ABCHelloWorldXYZ";
		assertConversion(string, cc, cc, string);
	}

	@Test
	public void camel2snake() {
		assertConversion("abc-hello-world-xyz", new SnakeCase("-", false), CamelCase.create(), "ABCHelloWorldXYZ");
	}

	@Test
	public void snake2camel() {
		assertConversion("AbcHelloWorldXyz", CamelCase.create(), new SnakeCase("-", false), "abc-hello-world-xyz");
	}

	@Test
	public void snake2cased() {
		assertConversion("hello_world", new SnakeCase("_", false), new SnakeCase(" ", true), "Hello World");
	}
}
