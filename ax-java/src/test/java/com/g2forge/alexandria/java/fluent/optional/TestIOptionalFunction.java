package com.g2forge.alexandria.java.fluent.optional;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.factory.OptionalFunctionFactory;
import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;
import com.g2forge.alexandria.java.function.IBiPredicate;

public class TestIOptionalFunction {
	@Test
	public void override() {
		final IOptionalFunction<Integer, String> override = IOptionalFunction.of(NullableOptional.FACTORY, 0, "Zero").override(IOptionalFunction.of(NullableOptional.FACTORY, 1, "X")).override(IOptionalFunction.of(NullableOptional.FACTORY, 1, "One"));
		Assert.assertEquals("Zero", override.apply(0).get());
		Assert.assertEquals("One", override.apply(1).get());
		Assert.assertTrue(override.apply(2).isEmpty());
	}

	@Test
	public void recursive() {
		final IOptionalFunction<Integer, Integer> function = new OptionalFunctionFactory<Integer, Integer>().build().add(0, 1).add(1, 2).build();
		Assert.assertEquals(1, function.apply(0).get().intValue());
		Assert.assertEquals(2, function.apply(1).get().intValue());
		final IOptionalFunction<Integer, Integer> recursivePrior = function.recursive(IBiPredicate.create(false), true, Integer.class);
		Assert.assertEquals(2, recursivePrior.apply(0).get().intValue());
		Assert.assertEquals(2, recursivePrior.apply(1).get().intValue());

	}

	@Test
	public void recursiveNonPrior() {
		final IOptionalFunction<Integer, Object> function = new OptionalFunctionFactory<Integer, Object>().build().add(0, 1).add(1, 2).add(3, 4).add(4, "Hello").build();
		final IOptionalFunction<Integer, Object> recursive = function.recursive(IBiPredicate.create(false), false, Integer.class);
		Assert.assertTrue(recursive.apply(0).isEmpty());
		Assert.assertTrue(recursive.apply(1).isEmpty());
		Assert.assertEquals("Hello", recursive.apply(3).get());
		Assert.assertEquals("Hello", recursive.apply(4).get());
	}
}
