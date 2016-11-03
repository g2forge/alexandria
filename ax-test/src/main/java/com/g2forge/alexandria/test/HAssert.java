package com.g2forge.alexandria.test;

import org.junit.Assert;

import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAssert extends Assert {
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

	public static <T> IConsumer1<? super T> testEquals(T expected) {
		return IConsumer2.create(HAssert::assertEquals).curry0(expected);
	}

	public static <T> IConsumer1<? super T> testNotEquals(T unexpected) {
		return IConsumer2.create(HAssert::assertNotEquals).curry0(unexpected);
	}

	public static <T> IConsumer1<? super T> testInstanceOf(Class<?> expected) {
		return IConsumer2.create(HAssert::assertInstanceOf).curry0(expected);
	}
}
