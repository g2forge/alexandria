package com.g2forge.alexandria.command.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.command.exit.Exit;
import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.IEnvironment;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.ICommand;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.HIO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@FunctionalInterface
public interface IStandardCommand extends IStructuredCommand {
	@Data
	@Accessors(chain = true)
	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public static class Tester {
		protected final IStandardCommand command;

		protected InputStream standardInput = null;

		protected Path working = null;

		protected List<String> arguments = new ArrayList<>();

		protected IEnvironment environment = SystemEnvironment.create();

		public Tester addArguments(Collection<? extends String> arguments) {
			getArguments().addAll(arguments);
			return this;
		}

		public Tester addArguments(String... arguments) {
			getArguments().addAll(HCollection.asList(arguments));
			return this;
		}

		public TestResult invoke() throws Throwable {
			final ByteArrayOutputStream standardOutput = new ByteArrayOutputStream();
			final ByteArrayOutputStream standardError = new ByteArrayOutputStream();
			final StandardIO<InputStream, PrintStream> io = new StandardIO<>(getStandardInput(), new PrintStream(standardOutput), new PrintStream(standardError));
			final CommandInvocation<InputStream, PrintStream> invocation = new CommandInvocation<>(ICommandFormat.getDefault(), arguments, io, getWorking(), getEnvironment());
			final IExit exit = getCommand().invoke(invocation);
			return new TestResult(exit, new ByteArrayInputStream(standardOutput.toByteArray()), new ByteArrayInputStream(standardError.toByteArray()));
		}

		public Tester setStandardInput(InputStream standardInput) {
			this.standardInput = standardInput;
			return this;
		}

		public Tester setStandardInput(String standardInput) {
			setStandardInput(HIO.toInputStream(standardInput));
			return this;
		}
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class TestResult {
		protected final IExit exit;

		protected final InputStream standardOutput;

		protected final InputStream standardError;
	}

	IExit SUCCESS = new Exit(ICommand.SUCCESS);

	IExit FAIL = new Exit(ICommand.FAIL);

	static void main(String[] args, IStandardCommand command) throws Throwable {
		final CommandInvocation<InputStream, PrintStream> invocation = CommandInvocation.of(args);
		final IExit exit = command.invoke(invocation);
		System.exit(exit.getCode());
	}

	public static IStandardCommand of(IFunction1<? super CommandInvocation<InputStream, PrintStream>, ? extends IConstructorCommand> factory) {
		return invocation -> factory.apply(invocation).invoke();
	}

	public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable;

	public default TestResult test() throws Throwable {
		return tester().invoke();
	}

	public default TestResult test(InputStream standardInput) throws Throwable {
		return tester().setStandardInput(standardInput).invoke();
	}

	public default TestResult test(String... arguments) throws Throwable {
		return tester().addArguments(arguments).invoke();
	}

	public default Tester tester() throws Throwable {
		return new Tester(this);
	}
}