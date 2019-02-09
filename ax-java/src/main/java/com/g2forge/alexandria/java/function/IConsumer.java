package com.g2forge.alexandria.java.function;

public interface IConsumer extends IFunctional {
	public IConsumer sync(Object lock);

	public <O> IFunction<O> toFunction(O retVal);

	public IConsumer wrap(IRunnable pre, IRunnable post);
}
