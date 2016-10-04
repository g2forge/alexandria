package com.g2forge.alexandria.java.fluent.optional;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;

public class TestIOptionalFunction {
	@Test
	public void override() {
		final IOptionalFunction<Integer, String> override = IOptionalFunction.of(NullableOptional.FACTORY, 0, "Zero").override(IOptionalFunction.of(NullableOptional.FACTORY, 1, "One"));
		Assert.assertEquals("Zero", override.apply(0).get());
		Assert.assertEquals("One", override.apply(1).get());
		Assert.assertTrue(override.apply(2).isEmpty());
	}
}
