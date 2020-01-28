package com.g2forge.alexandria.wizard;

import java.io.InputStream;
import java.io.PrintStream;

import com.g2forge.alexandria.command.CommandInvocation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class CommandLineStringInput extends AInput<String> {
	protected final CommandInvocation<InputStream, PrintStream> invocation;

	protected final int index;

	@Override
	public String get() {
		if (isEmpty()) { throw new InputUnspecifiedException("There were too few command line arguments!"); }
		return getInvocation().getArguments().get(getIndex());
	}

	@Override
	public boolean isEmpty() {
		return getInvocation().getArguments().size() <= getIndex();
	}
}
