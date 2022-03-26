package com.g2forge.alexandria.java.retry;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;

public class TestRetry {
	@Test
	public void run() {
		final int[] count = new int[] { 0 };
		new Retry(Duration.ofMillis(200), Duration.ofMillis(1), () -> {
			if (++count[0] >= 8) return;
			throw new RuntimeException();
		}).run();
		Assert.assertTrue(Integer.toString(count[0]), count[0] >= 8);
		Assert.assertTrue(Integer.toString(count[0]), count[0] <= 10);
	}

	@Test
	public void runOnce() {
		final int[] count = new int[] { 0 };
		new Retry(Duration.ZERO, () -> count[0]++).run();
		Assert.assertEquals(1, count[0]);
	}
}
