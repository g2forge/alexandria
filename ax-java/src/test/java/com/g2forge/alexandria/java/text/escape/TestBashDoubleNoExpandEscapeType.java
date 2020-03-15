package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import com.g2forge.alexandria.java.text.escape.v2.BashEscapeType;
import com.g2forge.alexandria.java.text.escape.v2.IEscaper;

import lombok.AccessLevel;
import lombok.Getter;

public class TestBashDoubleNoExpandEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = BashEscapeType.DoubleNoExpand.getEscaper();

	@Test
	public void backslash() {
		test(" \\\\", " \\");
	}

	@Test
	public void backtick() {
		test("a\\`echo b\\`c", "a`echo b`c");
	}

	@Test
	public void quote() {
		test("\\\"", "\"");
	}

	@Test
	public void variable() {
		test("\\${X}", "${X}");
	}
}
