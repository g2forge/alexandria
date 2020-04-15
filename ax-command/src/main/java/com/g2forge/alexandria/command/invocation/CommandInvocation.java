package com.g2forge.alexandria.command.invocation;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.g2forge.alexandria.command.stdio.IStandardIO;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class CommandInvocation<I, O> {
	public static final CommandInvocation<InputStream, PrintStream> of(String... args) {
		final CommandInvocation.CommandInvocationBuilder<InputStream, PrintStream> retVal = CommandInvocation.builder();
		retVal.arguments(HCollection.asList(args));
		retVal.io(StandardIO.of());
		retVal.working(Paths.get(System.getProperty("user.dir")));
		return retVal.build();
	}

	@Singular
	protected final List<String> arguments;

	protected final IStandardIO<I, O> io;

	protected final Path working;
}
