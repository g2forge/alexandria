package com.g2forge.alexandria.analysis;

import com.g2forge.alexandria.java.function.IFunction;

public interface ISerializableFunction<O> extends IFunction<O>, ISerializableLambda {
	public default O executeWith(IInjectingExecutor executor) {
		return executor.execute(this);
	}
}