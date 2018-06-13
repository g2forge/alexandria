package com.g2forge.alexandria.command;

import java.io.InputStream;
import java.io.PrintStream;

import com.g2forge.alexandria.java.function.IFunction1;

@FunctionalInterface
public interface IConstructorCommand extends IStructuredCommand {
	public static void main(String[] args, IFunction1<? super Invocation<InputStream, PrintStream>, ? extends IConstructorCommand> factory) throws Throwable {
		IStructuredCommand.main(args, IStandardCommand.of(factory));
	}

	public int invoke() throws Throwable;
}
