package com.g2forge.alexandria.adt.range;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.adt.range.IntegerRange;

public class TestIntegerRange {
	@Test
	public void reversed() {
		Assert.assertFalse(new IntegerRange(0, 1).isReversed());
		Assert.assertFalse(new IntegerRange(0, 0).isReversed());
		Assert.assertTrue(new IntegerRange(0, -1).isReversed());
	}

	@Test
	public void wrapSubRange() {
		final IntegerRange range = new IntegerRange(0, 2);
		Assert.assertEquals(new IntegerRange(0, 2), range.wrapSubRange(new IntegerRange(0, 2)));
		Assert.assertEquals(new IntegerRange(0, 2), range.wrapSubRange(new IntegerRange(0, -1)));
		Assert.assertEquals(new IntegerRange(1, 2), range.wrapSubRange(new IntegerRange(-2, -1)));
		Assert.assertEquals(new IntegerRange(0, 1), range.wrapSubRange(new IntegerRange(-3, -2)));
	}

	@Test
	public void validate() {
		new IntegerRange(0, 0).validateSubRange(new IntegerRange(0, 0));
		new IntegerRange(0, 1).validateSubRange(new IntegerRange(0, 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void validateFail() {
		new IntegerRange(0, 1).validateSubRange(new IntegerRange(1, 1));
	}
}
