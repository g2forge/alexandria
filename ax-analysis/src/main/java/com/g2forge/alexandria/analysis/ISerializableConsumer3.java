package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IConsumer3;

public interface ISerializableConsumer3<I0, I1, I2> extends IConsumer3<I0, I1, I2>, SerializableLambda, ISerializableConsumer {
	public static <I0, I1, I2> IMethodAnalyzer analyze(ISerializableConsumer3<I0, I1, I2> lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static <I0, I1, I2> ISerializableConsumer3<I0, I1, I2> create(ISerializableConsumer3<I0, I1, I2> consumer) {
		return consumer;
	}
}