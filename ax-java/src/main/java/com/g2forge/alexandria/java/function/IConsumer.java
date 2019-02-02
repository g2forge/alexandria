package com.g2forge.alexandria.java.function;

public interface IConsumer {
	public <O> IFunction<O> toFunction(O retVal);
}
