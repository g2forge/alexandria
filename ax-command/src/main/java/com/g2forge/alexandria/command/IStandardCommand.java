package com.g2forge.alexandria.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@FunctionalInterface
public interface IStandardCommand extends IStructuredCommand {
	@Data
	@Builder
	@AllArgsConstructor
	public static class TestResult {
		protected final int exitCode;

		protected final InputStream standardOutput;

		protected final InputStream standardError;
	}

	public static IStandardCommand of(IFunction1<? super CommandInvocation, ? extends IConstructorCommand> factory) {
		return invocation -> factory.apply(invocation).invoke();
	}

	public static IStandardCommand of(ISupplier<? extends IArgsCommand> supplier) {
		return invocation -> supplier.get().invoke(invocation.getArguments().toArray(new String[0]));
	}

	public int invoke(CommandInvocation invocation) throws Throwable;

	public default TestResult test(InputStream standardInput, Path working, String... arguments) throws Throwable {
		final ByteArrayOutputStream standardOutput = new ByteArrayOutputStream();
		final ByteArrayOutputStream standardError = new ByteArrayOutputStream();
		final StandardIO<InputStream, PrintStream> io = new StandardIO<>(standardInput, new PrintStream(standardOutput), new PrintStream(standardError));
		final CommandInvocation invocation = new CommandInvocation(HCollection.asList(arguments), io, working);
		final int exitCode = invoke(invocation);
		return new TestResult(exitCode, new ByteArrayInputStream(standardOutput.toByteArray()), new ByteArrayInputStream(standardError.toByteArray()));
	}
}