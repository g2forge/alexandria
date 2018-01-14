package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IConsumer2;

public interface ISerializableConsumer2<I0, I1> extends IConsumer2<I0, I1>, SerializableLambda, ISerializableConsumer {
	public static <I0, I1> IMethodAnalyzer analyze(ISerializableConsumer2<I0, I1> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I0, I1> ISerializableConsumer2<I0, I1> create(ISerializableConsumer2<I0, I1> consumer) {
		return consumer;
	}
}