package com.g2forge.alexandria.java.core.helpers;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestCollectionHelpers {
	@Test
	public void getLast() {
		Assert.assertEquals(-1, HCollection.getLast(Arrays.asList(1, 2, 3), v -> false));
		Assert.assertEquals(2, HCollection.getLast(Arrays.asList(1, 2, 3), v -> v > 2));
		Assert.assertEquals(0, HCollection.getLast(Arrays.asList(1, 2, 3), v -> v < 2));
	}
}
