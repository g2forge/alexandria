package com.g2forge.alexandria.java.core.error;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.concurrent.RuntimeInterruptedException;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector.SimpleCollector;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.ISupplier;
import com.g2forge.alexandria.java.function.IThrowConsumer1;
import com.g2forge.alexandria.java.function.IThrowFunction1;
import com.g2forge.alexandria.java.function.IThrowRunnable;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HError {
	public static <T, R, _T extends Throwable> Collector<OrThrowable<? extends T>, ?, R> collector(Supplier<_T> supplier, boolean allowPartial, Collector<? super T, ?, R> collector) {
		return new SimpleCollector<OrThrowable<? extends T>, List<OrThrowable<? extends T>>, R>(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, list -> {
			final Map<Boolean, List<OrThrowable<? extends T>>> grouped = list.stream().collect(Collectors.groupingBy(OrThrowable::isNotEmpty));
			final List<OrThrowable<? extends T>> successes = grouped.get(true);
			if (!(allowPartial && (successes != null) && !successes.isEmpty())) {
				final List<OrThrowable<? extends T>> failures = grouped.get(false);
				if ((failures != null) && !failures.isEmpty()) HError.throwQuietly(HError.createWithSuppressed(supplier, failures.stream().map(OrThrowable::getThrowable).collect(Collectors.toList())));
			}
			return ((successes == null) ? Stream.<T>empty() : successes.stream().map(OrThrowable::get)).collect(collector);
		}, Collections.emptySet());
	}

	public static <T extends Throwable> Collector<Throwable, ?, Void> collector(Supplier<T> supplier, boolean allowPartial) {
		return new SimpleCollector<Throwable, List<Throwable>, Void>(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, list -> {
			if (allowPartial && list.contains(null)) return null;
			final List<Throwable> throwables = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
			if (!throwables.isEmpty()) HError.throwQuietly(HError.createWithSuppressed(supplier, throwables));
			return null;
		}, Collections.emptySet());
	}

	public static <T extends Throwable> T createWithSuppressed(Supplier<? extends T> supplier, Iterable<? extends Throwable> throwables) {
		return withSuppressed(supplier.get(), throwables);
	}

	public static <T extends Throwable> T createWithSuppressed(Supplier<? extends T> supplier, Throwable... throwables) {
		return withSuppressed(supplier.get(), throwables);
	}

	public static void throwQuietly(ISupplier<String> messageSupplier, Collection<? extends Throwable> throwables) {
		if (throwables.isEmpty()) return;
		if (throwables.size() == 1) throwQuietly(HCollection.getOne(throwables));
		throw withSuppressed(messageSupplier == null ? new RuntimeWrappingException() : new RuntimeWrappingException(messageSupplier.get()), throwables);
	}

	public static void throwQuietly(Throwable throwable) {
		if (throwable instanceof Error) throw (Error) throwable;
		if (throwable instanceof RuntimeException) throw (RuntimeException) throwable;
		if (throwable instanceof IOException) throw new RuntimeIOException((IOException) throwable);
		if (throwable instanceof InterruptedException) throw new RuntimeInterruptedException((InterruptedException) throwable);
		throw new RuntimeWrappingException(throwable);
	}

	public static String toString(Throwable throwable) {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		throwable.printStackTrace(new PrintStream(stream));
		return stream.toString();
	}

	public static <T extends Throwable> T withSuppressed(final T exception, Iterable<? extends Throwable> suppressed) {
		if (suppressed != null) for (Throwable throwable : suppressed) {
			exception.addSuppressed(throwable);
		}
		return exception;
	}

	public static <T extends Throwable> T withSuppressed(final T exception, Throwable... suppressed) {
		if (suppressed != null) for (Throwable throwable : suppressed) {
			exception.addSuppressed(throwable);
		}
		return exception;
	}

	public static <I> Function<I, Throwable> wrap(Consumer<? super I> consumer) {
		return i -> {
			try {
				consumer.accept(i);
			} catch (Throwable throwable) {
				return throwable;
			}
			return null;
		};
	}

	public static <I, O> Function<I, OrThrowable<O>> wrap(Function<? super I, ? extends O> function) {
		return i -> {
			try {
				return new OrThrowable<>(function.apply(i));
			} catch (Throwable throwable) {
				return new OrThrowable<>(throwable);
			}
		};
	}

	public static <I> Function<I, Throwable> wrap(IThrowConsumer1<? super I, ?> consumer) {
		return i -> {
			try {
				consumer.accept(i);
			} catch (Throwable throwable) {
				return throwable;
			}
			return null;
		};
	}

	public static <I, O> Function<I, OrThrowable<O>> wrap(IThrowFunction1<? super I, ? extends O, ?> function) {
		return i -> {
			try {
				return new OrThrowable<>(function.apply(i));
			} catch (Throwable throwable) {
				return new OrThrowable<>(throwable);
			}
		};
	}

	public static Supplier<Throwable> wrap(IThrowRunnable<?> runnable) {
		return () -> {
			try {
				runnable.run();
			} catch (Throwable throwable) {
				return throwable;
			}
			return null;
		};
	}
}
