package com.g2forge.alexandria.command.invocation;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.g2forge.alexandria.command.invocation.environment.IEnvironment;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.stdio.IStandardIO;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CommandInvocation<A, I, O> {
	public static class CommandInvocationBuilder<A, I, O> implements IBuilder<CommandInvocation<A, I, O>> {}

	public static CommandInvocation<String, InputStream, PrintStream> of(String... args) {
		final CommandInvocation.CommandInvocationBuilder<String, InputStream, PrintStream> retVal = CommandInvocation.builder();
		retVal.format(ICommandFormat.getDefault());
		retVal.type(StringCommandArgumentType.create());
		retVal.arguments(HCollection.asList(args));
		retVal.io(StandardIO.of());
		retVal.working(Paths.get(System.getProperty("user.dir")));
		retVal.environment(SystemEnvironment.create());
		return retVal.build();
	}

	protected final ICommandFormat format;

	protected final ICommandArgumentType<A> type;

	@Singular
	protected final List<A> arguments;

	protected final IStandardIO<I, O> io;

	protected final Path working;

	protected final IEnvironment environment;

	public Path getArgumentAsPath(int index) {
		final String string = getArgumentAsString(index);
		if (string == null) return null;

		final Path path = Paths.get(string);
		if (path.isAbsolute()) return path;
		final Path working = getWorking();
		if (working == null) return path;
		return working.resolve(path);
	}

	public String getArgumentAsString(int index) {
		final A argument = getArguments().get(index);
		if (argument == null) return null;
		return getType().get(argument);
	}
}
