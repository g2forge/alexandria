package com.g2forge.alexandria.java.function;

public interface IFunction<O> {
	public IFunction<O> sync(Object lock);

	public IConsumer toConsumer();
}
