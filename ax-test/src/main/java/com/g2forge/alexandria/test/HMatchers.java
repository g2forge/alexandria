package com.g2forge.alexandria.test;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMatchers extends Matchers {
	public static <T extends Throwable> Matcher<Throwable> isThrowable(Class<T> type, Matcher<String> message) {
		return IsThrowable.isThrowable(type, message);
	}

	public static <T extends Throwable> Matcher<Throwable> isThrowable(Class<T> type, String message) {
		return IsThrowable.isThrowable(type, message);
	}
}
