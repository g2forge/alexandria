package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IConsumer1;

public interface ISerializableConsumer1<I> extends IConsumer1<I>, SerializableLambda, ISerializableConsumer {
	public static <I> IMethodAnalyzer analyze(ISerializableConsumer1<I> lambda) {
		return new MethodAnalyzer(lambda);
	}
	
	public static <I> ISerializableConsumer1<I> create(ISerializableConsumer1<I> consumer) {
		return consumer;
	}
}