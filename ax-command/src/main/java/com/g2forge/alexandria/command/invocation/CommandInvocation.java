package com.g2forge.alexandria.command.invocation;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.stdio.IStandardIO;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Wither;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Wither
public class CommandInvocation<I, O> {
	public static class CommandInvocationBuilder<I, O> implements IBuilder<CommandInvocation<I, O>> {}

	public static final CommandInvocation<InputStream, PrintStream> of(String... args) {
		final CommandInvocation.CommandInvocationBuilder<InputStream, PrintStream> retVal = CommandInvocation.builder();
		retVal.format(ICommandFormat.getDefault());
		retVal.arguments(HCollection.asList(args));
		retVal.io(StandardIO.of());
		retVal.working(Paths.get(System.getProperty("user.dir")));
		return retVal.build();
	}

	protected final ICommandFormat format;

	@Singular
	protected final List<String> arguments;

	protected final IStandardIO<I, O> io;

	protected final Path working;
}
