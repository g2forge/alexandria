package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.function.ISupplier;

@FunctionalInterface
public interface IArgsCommand extends IStructuredCommand {
	public static void main(String[] args, ISupplier<? extends IArgsCommand> factory) throws Throwable {
		IStructuredCommand.main(args, IStandardCommand.of(factory));
	}

	public int invoke(String... args) throws Throwable;
}
