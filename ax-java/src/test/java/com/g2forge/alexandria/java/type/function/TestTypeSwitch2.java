package com.g2forge.alexandria.java.type.function;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.IFunction2;

public class TestTypeSwitch2 {
	@Test
	public void basic() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		Assert.assertEquals("si: hello1", typeSwitch.apply("hello", 1));
		Assert.assertEquals("is: 2world", typeSwitch.apply(2, "world"));
	}

	/**
	 * Test what happens when there are 0 valid implementations for the combination of arguments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error0combo() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		typeSwitch.apply("", "");
	}

	/**
	 * Test what happens when there are 0 valid implementations for either argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error0either() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, String.class, (i0, i1) -> null);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		typeSwitch.apply(0, 1);
	}

	/**
	 * Test what happens when there are 0 valid implementations for the first argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error0i0() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, String.class, (i0, i1) -> null);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		typeSwitch.apply(0, "");
	}

	/**
	 * Test what happens when there are 0 valid implementations for the second argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error0i1() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, String.class, (i0, i1) -> null);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		typeSwitch.apply("", 1);
	}

	/**
	 * Test what happens when there are 2 valid implementations for the combination of arguments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error2combo() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(Number.class, Integer.class, (i0, i1) -> "ni: " + i0 + i1);
		builder.add(Integer.class, Number.class, (i0, i1) -> "in: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		typeSwitch.apply(0, 0);
	}

	@Test
	public void fallback() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		builder.fallback((i0, i1) -> "oo: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();
		Assert.assertEquals("oo: helloworld", typeSwitch.apply("hello", "world"));
	}
}
