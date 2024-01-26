package com.g2forge.alexandria.command.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.command.command.IStandardCommand.TestResult;
import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.MapEnvironment;
import com.g2forge.alexandria.command.stdio.IStandardIO;
import com.g2forge.alexandria.java.io.HTextIO;

public class TestStandardCommand {
	public static class Cat implements IStandardCommand {
		@Override
		public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
			final IStandardIO<InputStream, PrintStream> io = invocation.getIo();
			final InputStream input = io.getStandardInput();
			final PrintStream output = io.getStandardOutput();
			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
				while (true) {
					final String line = reader.readLine();
					if (line == null) break;
					output.println(line);
				}
			}
			return IStandardCommand.SUCCESS;
		}
	}

	public static class Echo implements IStandardCommand {
		@Override
		public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
			final PrintStream output = invocation.getIo().getStandardOutput();
			invocation.getArguments().forEach(output::print);
			return IStandardCommand.SUCCESS;
		}
	}

	public static class EnvVar implements IStandardCommand {
		@Override
		public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
			final PrintStream output = invocation.getIo().getStandardOutput();
			output.print(invocation.getEnvironment().apply(getClass().getSimpleName()));
			return IStandardCommand.SUCCESS;
		}
	}

	@Test
	public void cat() throws Throwable {
		final String message = "Hello, World!\nFoobar!\n";
		final TestResult result = new Cat().tester().setStandardInput(message).invoke();
		Assert.assertEquals(IStandardCommand.SUCCESS, result.getExit());
		Assert.assertEquals(message.replace("\n", System.lineSeparator()), HTextIO.readAll(result.getStandardOutput(), false));
	}

	@Test
	public void echo() throws Throwable {
		final String message = "Hello, World!";
		final TestResult result = new Echo().test(message);
		Assert.assertEquals(IStandardCommand.SUCCESS, result.getExit());
		Assert.assertEquals(message, HTextIO.readAll(result.getStandardOutput(), false));
	}

	@Test
	public void envVar() throws Throwable {
		final String message = "Hello, World!";
		final TestResult result = new EnvVar().tester().setEnvironment(MapEnvironment.builder().variable(EnvVar.class.getSimpleName(), message).build()).invoke();
		Assert.assertEquals(IStandardCommand.SUCCESS, result.getExit());
		Assert.assertEquals(message, HTextIO.readAll(result.getStandardOutput(), false));
	}
}
