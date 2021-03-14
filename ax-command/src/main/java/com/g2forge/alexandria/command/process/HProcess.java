package com.g2forge.alexandria.command.process;

import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.IEnvironment;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.command.process.cmdline.HCommandLineBuilder;
import com.g2forge.alexandria.command.process.cmdline.ICommandLineBuilder;
import com.g2forge.alexandria.command.stdio.IStandardIO;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HProcess {
	public static ProcessBuilder createProcessBuilder(CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation) {
		final ProcessBuilder builder = new ProcessBuilder();
		working(builder, invocation);
		arguments(builder, invocation);
		redirects(builder, invocation);
		environment(builder, invocation);
		return builder;
	}

	protected static void environment(final ProcessBuilder builder, CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation) {
		final IEnvironment environment = invocation.getEnvironment();
		if (environment == null) return;
		if (environment instanceof SystemEnvironment) return;

		final Map<String, String> map = builder.environment();
		map.clear();
		map.putAll(environment.toMap());
	}

	public static void working(final ProcessBuilder builder, CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation) {
		if (invocation.getWorking() != null) builder.directory(invocation.getWorking().toFile());
	}

	public static void redirects(final ProcessBuilder builder, CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation) {
		final IStandardIO<ProcessBuilder.Redirect, ProcessBuilder.Redirect> redirects = invocation.getIo();
		if (redirects != null) {
			final ProcessBuilder.Redirect standardInput = redirects.getStandardInput();
			if (standardInput != null) builder.redirectInput(standardInput);
			final ProcessBuilder.Redirect standardOutput = redirects.getStandardOutput();
			if (standardOutput != null) builder.redirectOutput(standardOutput);
			final ProcessBuilder.Redirect standardError = redirects.getStandardError();
			if (standardError != null) builder.redirectError(standardError);
		}
	}

	public static void arguments(final ProcessBuilder builder, CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation) {
		final ICommandLineBuilder commandLineBuilder = HCommandLineBuilder.getCommandLineBuilder();
		final List<String> line = commandLineBuilder.build(invocation.getFormat(), invocation.getArguments());
		builder.command(line);
	}
}
