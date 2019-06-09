package com.g2forge.alexandria.java.core.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HError {
	public static <T extends RuntimeException> T addSuppressed(final T exception, Iterable<? extends Throwable> suppressed) {
		for (Throwable throwable : suppressed) {
			exception.addSuppressed(throwable);
		}
		return exception;
	}

	public static <T extends RuntimeException> T addSuppressed(final T exception, Throwable... suppressed) {
		for (Throwable throwable : suppressed) {
			exception.addSuppressed(throwable);
		}
		return exception;
	}

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
		throw HError.multithrow(message, throwables);
	}

	public static RuntimeException multithrow(String message, Iterable<? extends Throwable> throwables) {
		return addSuppressed(new RuntimeException(message), throwables);
	}

	public static RuntimeException multithrow(String message, Throwable... throwables) {
		return addSuppressed(new RuntimeException(message), throwables);
	}
}
