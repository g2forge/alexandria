package com.g2forge.alexandria.java.text;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.IFunction1;

public class TestTextUpdateBuilder {
	@Test
	public void test() {
		final TextUpdateBuilder builder = new TextUpdateBuilder("abc");
		builder.accept(new TextUpdate<Object>(0, 0, IFunction1.create("x")));
		builder.accept(new TextUpdate<Object>(0, 3, s -> s.toString().toUpperCase()));
		Assert.assertEquals("xABC", builder.build());
	}
}
