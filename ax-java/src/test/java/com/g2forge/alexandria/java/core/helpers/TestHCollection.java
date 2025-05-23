package com.g2forge.alexandria.java.core.helpers;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestHCollection {
	@Test
	public void getLast() {
		Assert.assertEquals(-1, HCollection.getLast(Arrays.asList(1, 2, 3), v -> false));
		Assert.assertEquals(2, HCollection.getLast(Arrays.asList(1, 2, 3), v -> v > 2));
		Assert.assertEquals(0, HCollection.getLast(Arrays.asList(1, 2, 3), v -> v < 2));
	}

	@Test
	public void totalSize() {
		Assert.assertEquals(2, HCollection.totalSize(HCollection.asList("a", "b")));
		Assert.assertEquals(2, HCollection.totalSize(HCollection.asList("a"), HCollection.asList("a")));
		Assert.assertEquals(0, HCollection.totalSize(HCollection.emptyList(), null));
	}

	@Test
	public void xor() {
		Assert.assertEquals(HCollection.asList("a", "c"), HCollection.xor(HCollection.asList("a", "b"), HCollection.asList("b", "c")));
		Assert.assertEquals(HCollection.asList("a", "c"), HCollection.xor(HCollection.asList("a", "b"), HCollection.asList("b", "c"), HCollection.asList("b")));
		Assert.assertEquals(HCollection.asList("a"), HCollection.xor(HCollection.asList("a"), HCollection.asList("b", "c"), HCollection.asList("b", "c")));
		Assert.assertEquals(HCollection.asList("a"), HCollection.xor(HCollection.asList("a", "b", "b")));
	}
}
