package com.g2forge.alexandria.java.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.optional.OptionalFunction;

public class TestFunctionHelpers {
	@Test
	public void override() {
		final Map<String, Integer> map = new HashMap<>();
		map.put("A", 1);
		map.put("B", 2);
		Assert.assertEquals(1, map.get("A").intValue());
		Assert.assertEquals(2, map.get("B").intValue());

		final OptionalFunction<? super String, ? extends Integer> override = OptionalFunction.of(map).override(OptionalFunction.of("A", 3));
		Assert.assertEquals(3, override.apply("A").get().intValue());
		Assert.assertEquals(2, override.apply("B").get().intValue());
	}
}
