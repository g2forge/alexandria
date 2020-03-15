package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import lombok.AccessLevel;
import lombok.Getter;

public class TestJavaPatternCharClassEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = JavaEscapeType.PatternCharClass.getEscaper();

	@Test
	public void carrot() {
		test("\\^", "^");
	}

	@Test
	public void none() {
		test("abc", "abc");
	}

	@Test
	public void range() {
		test("\\[a\\-b\\]", "[a-b]");
	}
}
