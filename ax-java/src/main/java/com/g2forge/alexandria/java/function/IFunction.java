package com.g2forge.alexandria.java.function;

public interface IFunction<O> extends IConsumer {
	public IConsumer toConsumer();
}
