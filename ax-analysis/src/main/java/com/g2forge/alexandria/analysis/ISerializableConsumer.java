package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IConsumer;

public interface ISerializableConsumer extends IConsumer, ISerializableLambda {
	public default void executeWith(IInjectingExecutor executor) {
		executor.execute(this);
	}
}