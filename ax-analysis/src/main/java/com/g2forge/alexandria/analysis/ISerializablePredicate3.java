package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IPredicate3;

public interface ISerializablePredicate3<I0, I1, I2> extends IPredicate3<I0, I1, I2>, SerializableLambda, ISerializablePredicate {
	public static <I0, I1, I2> IMethodAnalyzer analyze(ISerializablePredicate3<I0, I1, I2> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I0, I1, I2> ISerializablePredicate3<I0, I1, I2> create(ISerializablePredicate3<I0, I1, I2> consumer) {
		return consumer;
	}
}