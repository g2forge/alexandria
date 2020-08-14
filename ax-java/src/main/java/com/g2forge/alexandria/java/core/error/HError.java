package com.g2forge.alexandria.java.core.error;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.g2forge.alexandria.java.concurrent.RuntimeInterruptedException;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IThrowRunnable;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HError {
	public static String toString(Throwable throwable) {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		throwable.printStackTrace(new PrintStream(stream));
		return stream.toString();
	}

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
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		throw HError.multithrow(message, throwables);
	}

	public static void multirun(String message, IThrowRunnable<?>... runnables) {
		final Collection<Throwable> throwables = new ArrayList<>();
		for (IThrowRunnable<?> runnable : runnables) {
			try {
				runnable.run();
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

	public static void throwQuietly(Throwable throwable) {
		if (throwable instanceof Error) throw (Error) throwable;
		if (throwable instanceof RuntimeException) throw (RuntimeException) throwable;
		if (throwable instanceof IOException) throw (RuntimeIOException) throwable;
		if (throwable instanceof InterruptedException) throw (RuntimeInterruptedException) throwable;
		throw new RuntimeWrappingException(throwable);
	}
}
