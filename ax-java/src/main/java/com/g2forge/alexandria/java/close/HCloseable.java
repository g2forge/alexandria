package com.g2forge.alexandria.java.close;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HCloseable {
	public static void close(AutoCloseable... closeables) {
		final List<Throwable> throwables = new ArrayList<>();
		for (AutoCloseable closeable : closeables) {
			try {
				closeable.close();
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		if (!throwables.isEmpty()) throw HError.withSuppressed(new RuntimeException(), throwables);
	}

	public static void close(Iterable<? extends AutoCloseable> closeables) {
		final List<Throwable> throwables = new ArrayList<>();
		for (AutoCloseable closeable : closeables) {
			try {
				closeable.close();
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		if (!throwables.isEmpty()) throw HError.withSuppressed(new RuntimeException(), throwables);
	}
}
