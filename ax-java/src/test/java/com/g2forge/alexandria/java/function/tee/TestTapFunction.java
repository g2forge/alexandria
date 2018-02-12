package com.g2forge.alexandria.java.function.tee;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestTapFunction {
	@Test
	public void test() {
		final List<Integer> list = new ArrayList<>();
		final TapFunction<Integer> function = new TapFunction<>(list::add);
		Assert.assertEquals(Integer.valueOf(0), function.apply(0));
		Assert.assertEquals(HCollection.asList(0), list);
	}
}
