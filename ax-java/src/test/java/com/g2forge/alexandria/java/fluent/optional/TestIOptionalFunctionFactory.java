package com.g2forge.alexandria.java.fluent.optional;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFunctionFactory;
import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;

public class TestIOptionalFunctionFactory {
	protected final IOptionalFunctionFactory<String, String, IOptionalFunction<String, String>> factory = new IOptionalFunctionFactory<String, String, IOptionalFunction<String, String>>() {
		@Override
		public IOptionalFunction<String, String> wrap(IOptionalFunction<String, String> function) {
			return function;
		}
	};

	@Test
	public void builder() {
		final IOptionalFunction<String, String> function = factory.build().add("a", "b").add("b", "c").build();
		Assert.assertEquals("b", function.apply("a").get());
		Assert.assertEquals("c", function.apply("b").get());
		Assert.assertTrue(function.apply("c").isEmpty());
	}

	@Test
	public void simple() {
		final IOptionalFunction<String, String> function = factory.of("Hello", "World");
		Assert.assertTrue(function.apply("a").isEmpty());
		Assert.assertEquals("World", function.apply("Hello").get());
	}
}
