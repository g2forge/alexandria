package com.g2forge.alexandria.java.text.escape;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.text.escape.IEscaper;
import com.g2forge.alexandria.java.text.escape.JavaEscapeType;

import lombok.AccessLevel;
import lombok.Getter;

public class TestJavaStringEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = JavaEscapeType.String.getEscaper();

	@Test
	public void first() {
		test("\\\\var", "\\var");
	}

	@Test
	public void last() {
		test("Hello\\n", "Hello\n");
	}

	@Test
	public void none() {
		test("Hello", "Hello");
	}

	@Test
	public void only() {
		test("\\\\", "\\");
	}

	@Test
	public void tab() {
		test("	", "	");
		Assert.assertEquals("	", escaper.unescape("\t"));
	}
}
