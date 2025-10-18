package com.g2forge.alexandria.workqueue;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestBasicWorkQueueFactory {
	@Test
	public void intial() {
		final boolean executed[] = { false };
		BasicWorkQueueFactory.create().run(new IWorkQueueHandler<Integer>() {
			@Override
			public void run(IWorkQueueContext<Integer> context, Integer task) {
				executed[task] = true;
			}
		}, 0).await();
		HAssert.assertTrue(executed[0]);
	}

	@Test
	public void noCancel() {
		final IWorkQueueHandle handle = BasicWorkQueueFactory.create().run(null);
		try {
			handle.cancel();
			HAssert.fail();
		} catch (IllegalStateException exception) {}
	}

	@Test
	public void order() {
		final int current[] = { 0 };
		BasicWorkQueueFactory.create().run(new IWorkQueueHandler<Integer>() {
			@Override
			public void run(IWorkQueueContext<Integer> context, Integer task) {
				if (task == 0) context.queue(2, 3);
				if (task == 1) context.queue(4);
				HAssert.assertEquals(current[0], (int) task);
				current[0]++;
			}
		}, 0, 1).await();
		HAssert.assertEquals(current[0], 5);
	}
}
