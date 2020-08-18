package com.g2forge.alexandria.java.adt.compare;

import org.junit.Assert;
import org.junit.Test;

public class TestComparableComparator {
	@Test
	public void equal() {
		Assert.assertEquals(0, ComparableComparator.<Integer>create().compare(0, 0));
		Assert.assertEquals(0, ComparableComparator.<Integer>create().compare(1, 1));
	}

	@Test
	public void geater() {
		Assert.assertEquals(1, ComparableComparator.<Integer>create().compare(0, -1));
		Assert.assertEquals(1, ComparableComparator.<Integer>create().compare(-805739, -329534290));
	}

	@Test
	public void less() {
		Assert.assertEquals(-1, ComparableComparator.<Integer>create().compare(0, 1));
		Assert.assertEquals(-1, ComparableComparator.<Integer>create().compare(10, 329534290));
	}

	@Test
	public void nulls() {
		Assert.assertEquals(0, ComparableComparator.<Integer>create().compare(null, null));
		Assert.assertEquals(-1, ComparableComparator.<Integer>create().compare(null, 0));
		Assert.assertEquals(1, ComparableComparator.<Integer>create().compare(0, null));
	}
}
