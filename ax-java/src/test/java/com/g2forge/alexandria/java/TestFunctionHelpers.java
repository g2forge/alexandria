package com.g2forge.alexandria.java;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.MapFunction;

public class TestFunctionHelpers {
	@Test
	public void override() {
		final Map<String, Integer> map = new HashMap<>();
		map.put("A", 1);
		map.put("B", 2);
		Assert.assertEquals(1, map.get("A").intValue());
		Assert.assertEquals(2, map.get("B").intValue());

		final MapFunction<? super String, ? extends Integer> override = MapFunction.of(map).override(MapFunction.of("A", 3));
		Assert.assertEquals(3, override.apply("A").get().intValue());
		Assert.assertEquals(2, override.apply("B").get().intValue());
	}
}
