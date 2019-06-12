package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IPredicate1;

public interface ISerializablePredicate1<I> extends IPredicate1<I>, SerializableLambda, ISerializablePredicate {
	public static <I> IMethodAnalyzer analyze(ISerializablePredicate1<I> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I> ISerializablePredicate1<I> create(ISerializablePredicate1<I> consumer) {
		return consumer;
	}
}