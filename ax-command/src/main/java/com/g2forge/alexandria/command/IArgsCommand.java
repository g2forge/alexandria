package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IArgsCommand extends ICommand {
	public static void main(String[] args, ISupplier<? extends IArgsCommand> factory) throws Throwable {
		final IArgsCommand command = factory.get();
		final int exitCode = command.invoke(args);
		System.exit(exitCode);
	}

	public int invoke(String... args) throws Throwable;
}
