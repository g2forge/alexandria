package com.g2forge.alexandria.analysis;

import java.util.function.Supplier;

@SuppressWarnings("hiding")
public interface SerializableSupplier<T> extends Supplier<T>, SerializableLambda {
	public static <T> IMethodAnalyzer analyze(SerializableSupplier<T> lambda) {
		return new MethodAnalyzer(lambda);
	}
}