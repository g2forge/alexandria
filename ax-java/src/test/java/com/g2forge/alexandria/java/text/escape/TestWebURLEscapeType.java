package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import lombok.AccessLevel;
import lombok.Getter;

public class TestWebURLEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = WebEscapeType.URL.getEscaper();

	@Test
	public void simple() {
		test("Hello+World%21", "Hello World!");
	}
}
