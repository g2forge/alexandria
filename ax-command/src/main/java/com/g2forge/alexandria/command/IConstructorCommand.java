package com.g2forge.alexandria.command;

import java.io.InputStream;
import java.io.PrintStream;

import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.java.function.IFunction1;

@FunctionalInterface
public interface IConstructorCommand extends IStructuredCommand {
	public static void main(String[] args, IFunction1<? super Invocation<InputStream, PrintStream>, ? extends IConstructorCommand> factory) throws Throwable {
		IStandardCommand.main(args, IStandardCommand.of(factory));
	}

	public IExit invoke() throws Throwable;
}
