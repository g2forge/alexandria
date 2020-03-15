package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import com.g2forge.alexandria.java.text.escape.BashEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscaper;

import lombok.AccessLevel;
import lombok.Getter;

public class TestBashDoubleExpandEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = BashEscapeType.DoubleExpand.getEscaper();

	@Test
	public void backslash() {
		test(" \\\\", " \\");
	}

	@Test
	public void backtick() {
		test("a`echo b`c", "a`echo b`c");
	}

	@Test
	public void quote() {
		test("\\\"", "\"");
	}

	@Test
	public void variable() {
		test("${X}", "${X}");
	}
}
