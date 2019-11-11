package com.g2forge.alexandria.test;

import org.junit.AssumptionViolatedException;
import org.junit.Test;

public class TestHAssume {
	@Test
	public void fail() {
		HAssert.assertException(AssumptionViolatedException.class, () -> HAssume.assumeNoException(() -> {
			throw new Error();
		}));
	}

	@Test
	public void success() {
		HAssert.assertEquals("A", HAssume.assumeNoException(() -> "A"));
	}
}
