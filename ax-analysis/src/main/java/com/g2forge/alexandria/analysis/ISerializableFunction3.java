package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IFunction3;

public interface ISerializableFunction3<I0, I1, I2, O> extends IFunction3<I0, I1, I2, O>, SerializableLambda, ISerializableFunction<O> {
	public static <I0, I1, I2, O> IMethodAnalyzer analyze(ISerializableFunction3<I0, I1, I2, O> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I0, I1, I2, O> ISerializableFunction3<I0, I1, I2, O> create(ISerializableFunction3<I0, I1, I2, O> function) {
		return function;
	}
}