package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IRunnable;

public interface ISerializableRunnable extends IRunnable, SerializableLambda {
	public static IMethodAnalyzer analyze(ISerializableRunnable lambda) {
		return new MethodAnalyzer(lambda);
	}

	public static ISerializableRunnable create(ISerializableRunnable supplier) {
		return supplier;
	}
}