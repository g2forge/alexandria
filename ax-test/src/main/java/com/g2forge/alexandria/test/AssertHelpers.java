package com.g2forge.alexandria.test;

import org.junit.Assert;

import com.g2forge.alexandria.java.function.FunctionHelpers;
import com.g2forge.alexandria.java.function.IConsumer;

public class AssertHelpers extends Assert {
	public static void assertException(Class<? extends Throwable> type, Runnable runnable) {
		try {
			runnable.run();
		} catch (Throwable throwable) {
			if (!type.isInstance(throwable)) throw throwable;
			else return;
		}
		fail("Did not receive expected exception of type " + type);
	}

	public static void assertInstanceOf(Class<?> expected, Object actual) {
		if (!expected.isInstance(actual)) {
			fail("Expected an instance of " + expected + " was \"" + actual + "\"!");
		}
	}

	public static <T> IConsumer<? super T> testEquals(T expected) {
		return FunctionHelpers.create(AssertHelpers::assertEquals).curry0(expected);
	}

	public static <T> IConsumer<? super T> testNotEquals(T unexpected) {
		return FunctionHelpers.create(AssertHelpers::assertNotEquals).curry0(unexpected);
	}

	public static <T> IConsumer<? super T> testInstanceOf(Class<?> expected) {
		return FunctionHelpers.create(AssertHelpers::assertInstanceOf).curry0(expected);
	}
}
