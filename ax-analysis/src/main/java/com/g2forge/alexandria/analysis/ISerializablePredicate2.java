package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IPredicate2;

public interface ISerializablePredicate2<I0, I1> extends IPredicate2<I0, I1>, SerializableLambda, ISerializablePredicate {
	public static <I0, I1> IMethodAnalyzer analyze(ISerializablePredicate2<I0, I1> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I0, I1> ISerializablePredicate2<I0, I1> create(ISerializablePredicate2<I0, I1> consumer) {
		return consumer;
	}
}