package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAnalysis {
	public static java.lang.reflect.Method getMethod(final ISerializableFunction1<?, ?> lambda) {
		return ISerializableFunction1.analyze(lambda).getMethod();
	}

	public static String getPath(final ISerializableSupplier<?> lambda) {
		return ISerializableSupplier.analyze(lambda).getPath();
	}

	public static java.lang.reflect.Method getMethod(final ISerializableSupplier<?> lambda) {
		return ISerializableSupplier.analyze(lambda).getMethod();
	}

	public static String getPath(final ISerializableFunction1<?, ?> lambda) {
		return ISerializableFunction1.analyze(lambda).getPath();
	}
}
