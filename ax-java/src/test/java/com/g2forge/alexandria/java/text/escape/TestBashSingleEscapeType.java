package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import com.g2forge.alexandria.java.text.escape.v2.BashEscapeType;
import com.g2forge.alexandria.java.text.escape.v2.IEscaper;

import lombok.AccessLevel;
import lombok.Getter;

public class TestBashSingleEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = BashEscapeType.Single.getEscaper();

	@Test
	public void first() {
		test("'\"'\"'var", "'var");
	}

	@Test
	public void none() {
		test("Hello", "Hello");
	}

	@Test
	public void only() {
		test("'\"'\"'", "'");
	}
}
