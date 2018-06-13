package com.g2forge.alexandria.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.command.IStandardCommand.TestResult;
import com.g2forge.alexandria.java.core.iface.ICommand;
import com.g2forge.alexandria.java.io.HIO;

public class TestStandardCommand {
	public static class Cat implements IStandardCommand {
		@Override
		public int invoke(CommandInvocation invocation) throws Throwable {
			final StandardIO<InputStream, PrintStream> io = invocation.getIo();
			final InputStream input = io.getStandardInput();
			final PrintStream output = io.getStandardOutput();
			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
				while (true) {
					final String line = reader.readLine();
					if (line == null) break;
					output.println(line);
				}
			}
			return SUCCESS;
		}
	}

	public static class Echo implements IStandardCommand {
		@Override
		public int invoke(CommandInvocation invocation) throws Throwable {
			final PrintStream output = invocation.getIo().getStandardOutput();
			invocation.getArguments().forEach(output::print);
			return SUCCESS;
		}
	}

	@Test
	public void cat() throws Throwable {
		final String message = "Hello, World!\nFoobar!\n";
		final TestResult result = new Cat().test(HIO.toInputStream(message), null);
		Assert.assertEquals(ICommand.SUCCESS, result.getExitCode());
		Assert.assertEquals(message.replace("\n", System.lineSeparator()), HIO.readAll(result.getStandardOutput(), false));
	}

	@Test
	public void echo() throws Throwable {
		final String message = "Hello, World!";
		final TestResult result = new Echo().test(null, null, message);
		Assert.assertEquals(ICommand.SUCCESS, result.getExitCode());
		Assert.assertEquals(message, HIO.readAll(result.getStandardOutput(), false));
	}
}
