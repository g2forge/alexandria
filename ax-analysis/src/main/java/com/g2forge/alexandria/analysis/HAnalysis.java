package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAnalysis {
	public static java.lang.reflect.Method getMethod(final SerializableFunction<?, ?> lambda) {
		return SerializableFunction.analyze(lambda).getMethod();
	}

	public static String getPath(final SerializableSupplier<?> lambda) {
		return SerializableSupplier.analyze(lambda).getPath();
	}

	public static java.lang.reflect.Method getMethod(final SerializableSupplier<?> lambda) {
		return SerializableSupplier.analyze(lambda).getMethod();
	}

	public static String getPath(final SerializableFunction<?, ?> lambda) {
		return SerializableFunction.analyze(lambda).getPath();
	}
}
