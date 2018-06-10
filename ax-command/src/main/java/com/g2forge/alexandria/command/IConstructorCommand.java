package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.function.IFunction1;

@FunctionalInterface
public interface IConstructorCommand extends IStructuredCommand {
	public static void main(String[] args, IFunction1<? super CommandInvocation, ? extends IConstructorCommand> factory) throws Throwable {
		IStructuredCommand.main(args, IStandardCommand.of(factory));
	}

	public int invoke() throws Throwable;
}
