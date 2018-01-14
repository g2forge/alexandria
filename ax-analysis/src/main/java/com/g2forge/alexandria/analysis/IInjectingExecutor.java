package com.g2forge.alexandria.analysis;

public interface IInjectingExecutor {
	public void execute(ISerializableConsumer consumer);

	public <O> O execute(ISerializableFunction<O> function);
}
