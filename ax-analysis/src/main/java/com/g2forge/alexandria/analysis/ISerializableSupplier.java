package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.ISupplier;

@SuppressWarnings("hiding")
public interface ISerializableSupplier<T> extends ISupplier<T>, SerializableLambda {
	public static <T> IMethodAnalyzer analyze(ISerializableSupplier<T> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <T> ISerializableSupplier<T> create(ISerializableSupplier<T> supplier) {
		return supplier;
	}
}