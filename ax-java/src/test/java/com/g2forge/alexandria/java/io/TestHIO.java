package com.g2forge.alexandria.java.io;

import org.junit.Assert;
import org.junit.Test;

public class TestHIO {
	@Test
	public void closeAllException() {
		final boolean[] flags = new boolean[] { false, false, false };
		try {
			HIO.closeAll(() -> {
				flags[0] = true;
			}, () -> {
				throw new RuntimeException("Failed");
			}, () -> {
				flags[2] = true;
			});
			Assert.fail("Should have thrown an exception");
		} catch (Throwable throwable) {
			Assert.assertEquals("Failed", throwable.getSuppressed()[0].getMessage());
		}
		Assert.assertTrue(flags[0]);
		Assert.assertFalse(flags[1]);
		Assert.assertTrue(flags[2]);
	}

	@Test
	public void closeAllSuccess() {
		final boolean[] flags = new boolean[] { false, false, false };
		HIO.closeAll(() -> {
			flags[0] = true;
		}, () -> {
			flags[1] = true;
		}, () -> {
			flags[2] = true;
		});
		for (int i = 0; i < flags.length; i++) {
			Assert.assertTrue("Flag " + i, flags[i]);
		}
	}
}
