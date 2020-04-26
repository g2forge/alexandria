package com.g2forge.alexandria.test;

import java.util.Objects;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.ComparisonFailure;

import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.core.resource.HResource;
import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IThrowRunnable;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAssert extends Assert {
	public static <T extends Throwable> void assertThat(IThrowRunnable<T> operation, Matcher<Throwable> matcher) {
		try {
			operation.run();
		} catch (Throwable throwable) {
			MatcherAssert.assertThat("", throwable, matcher);
			return;
		}
		MatcherAssert.assertThat("", null, matcher);
	}

	public static <T extends Throwable> void assertException(Class<? extends Throwable> type, String message, IThrowRunnable<T> operation) throws T {
		try {
			operation.run();
		} catch (Throwable throwable) {
			if (type.isInstance(throwable)) {
				if (message != null) assertEquals(message, throwable.getMessage());
				return;
			} else throw throwable;
		}
		fail(String.format("Expected exception of type \"%1$s\" was not thrown!", type.getName()));
	}

	public static void assertException(Class<? extends Throwable> type, Runnable runnable) {
		assertException(type, null, runnable::run);
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

	public static <T> void assertEquals(String message, IFunction1<? super T, ? extends String> toString, T expected, T actual) {
		if (Objects.equals(expected, actual)) return;
		throw new ComparisonFailure(message, toString.apply(expected), toString.apply(actual));
	}

	protected static void assertEquals(String message, Object expected, Object actual, final String expectedString, final String actualString) {
		if (Objects.equals(expected, actual)) return;
		throw new ComparisonFailure(message, expectedString, actualString);
	}

	public static void assertEquals(Resource expected, String actual) {
		assertEquals(expected.read(true), actual);
	}
}
