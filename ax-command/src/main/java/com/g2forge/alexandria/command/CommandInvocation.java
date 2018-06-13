package com.g2forge.alexandria.command;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class CommandInvocation {
	public static final CommandInvocation of(String... args) {
		final CommandInvocation.CommandInvocationBuilder retVal = CommandInvocation.builder();
		retVal.arguments(HCollection.asList(args));
		retVal.io(StandardIO.of());
		retVal.working(Paths.get(System.getProperty("user.dir")));
		return retVal.build();
	}

	@Singular
	protected final List<String> arguments;

	protected final StandardIO<InputStream, PrintStream> io;

	protected final Path working;
}
