package com.g2forge.alexandria.test;

import org.junit.Assert;

public class AssertHelpers {
	public static void assertException(Class<? extends Throwable> type, Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable throwable) {
			if (!type.isInstance(throwable)) throw throwable;
			else return;
		}
		Assert.fail("Did not receive expected exception of type " + type);
	}
}
