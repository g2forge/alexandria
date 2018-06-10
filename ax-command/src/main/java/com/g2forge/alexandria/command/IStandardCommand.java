package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.ISupplier;

@FunctionalInterface
public interface IStandardCommand extends IStructuredCommand {
	public static IStandardCommand of(IFunction1<? super CommandInvocation, ? extends IConstructorCommand> factory) {
		return invocation -> factory.apply(invocation).invoke();
	}

	public static IStandardCommand of(ISupplier<? extends IArgsCommand> supplier) {
		return invocation -> supplier.get().invoke(invocation.getArguments().toArray(new String[0]));
	}

	public int invoke(CommandInvocation invocation) throws Throwable;
}