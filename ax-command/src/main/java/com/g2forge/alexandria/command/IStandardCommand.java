package com.g2forge.alexandria.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@FunctionalInterface
public interface IStandardCommand extends IStructuredCommand {
	@Data
	@Builder
	@AllArgsConstructor
	public static class TestResult {
		protected final IExit exit;

		protected final InputStream standardOutput;

		protected final InputStream standardError;
	}

	static void main(String[] args, IStandardCommand command) throws Throwable {
		final Invocation<InputStream, PrintStream> invocation = Invocation.of(args);
		final IExit exit = command.invoke(invocation);
		System.exit(exit.getCode());
	}

	public static IStandardCommand of(IFunction1<? super Invocation<InputStream, PrintStream>, ? extends IConstructorCommand> factory) {
		return invocation -> factory.apply(invocation).invoke();
	}

	public IExit invoke(Invocation<InputStream, PrintStream> invocation) throws Throwable;

	public default TestResult test(InputStream standardInput, Path working, String... arguments) throws Throwable {
		final ByteArrayOutputStream standardOutput = new ByteArrayOutputStream();
		final ByteArrayOutputStream standardError = new ByteArrayOutputStream();
		final StandardIO<InputStream, PrintStream> io = new StandardIO<>(standardInput, new PrintStream(standardOutput), new PrintStream(standardError));
		final Invocation<InputStream, PrintStream> invocation = new Invocation<>(HCollection.asList(arguments), io, working);
		final IExit exit = invoke(invocation);
		return new TestResult(exit, new ByteArrayInputStream(standardOutput.toByteArray()), new ByteArrayInputStream(standardError.toByteArray()));
	}
}