package com.g2forge.alexandria.java.core.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class ErrorHelpers {
	@SafeVarargs
	public static <T> void multiprocess(Consumer<? super T> consumer, String message, T... values) {
		final Collection<Throwable> throwables = new ArrayList<>();
		for (T value : values) {
			try {
				consumer.accept(value);
				return;
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		throw ErrorHelpers.multithrow(message, throwables);
	}

	public static RuntimeException multithrow(String message, Iterable<? extends Throwable> throwables) {
		final RuntimeException retVal = new RuntimeException(message);
		for (Throwable throwable : throwables)
			retVal.addSuppressed(throwable);
		return retVal;
	}

	public static RuntimeException multithrow(String message, Throwable... throwables) {
		final RuntimeException retVal = new RuntimeException(message);
		for (Throwable throwable : throwables)
			retVal.addSuppressed(throwable);
		return retVal;
	}
}
