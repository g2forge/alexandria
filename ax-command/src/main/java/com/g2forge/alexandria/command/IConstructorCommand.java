package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.function.IFunction1;

public interface IConstructorCommand extends ICommand {
	public static void main(String[] args, IFunction1<? super String[], ? extends IConstructorCommand> factory) throws Throwable {
		final IConstructorCommand command = factory.apply(args);
		final int exitCode = command.invoke();
		System.exit(exitCode);
	}

	public int invoke() throws Throwable;
}
