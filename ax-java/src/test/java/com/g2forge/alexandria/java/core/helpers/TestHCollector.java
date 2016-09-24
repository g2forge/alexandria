package com.g2forge.alexandria.java.core.helpers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

public class TestHCollector {
	@Test
	public void test() {
		final Function<? super String, ? extends Iterable<? extends String>> classifier = s -> HCollection.asList(s.split("\\s+"));
		final Map<String, List<String>> result = HCollection.asList("a x", "a y", "a z", "b x", "z z").stream().collect(HCollector.multiGroupingBy(classifier));
		Assert.assertEquals(HCollection.asList("a x", "a y", "a z"), result.get("a"));
		Assert.assertEquals(HCollection.asList("a x", "b x"), result.get("x"));
		Assert.assertEquals(HCollection.asList("a y"), result.get("y"));
		Assert.assertEquals(HCollection.asList("a z", "z z", "z z"), result.get("z"));
	}
}
