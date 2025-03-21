package com.g2forge.alexandria.command.invocation.runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.g2forge.alexandria.command.clireport.HCLIReport;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.process.HProcess;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.HTextIO;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;
import com.g2forge.alexandria.test.HAssert;

import lombok.Getter;

public class TestICommandRunner {
	protected static final String CLIREPORT_VERSION = "v0.0.1";

	protected static final String CLIREPORT_FILENAME = "clireport";

	@Getter
	protected Path cliReport;

	protected void assumeMicrosoft() {
		Assume.assumeTrue(PlatformCategory.Microsoft.equals(HPlatform.getPlatform().getCategory()));
	}

	protected void assumePosix() {
		Assume.assumeTrue(PlatformCategory.Posix.equals(HPlatform.getPlatform().getCategory()));
	}

	@Before
	public void before() {
		cliReport = HCLIReport.download(null).get();
	}

	@Test
	public void microsoftBasic() throws IOException, InterruptedException {
		assumeMicrosoft();
		test("a", "'", "%VAR%", "$env:VAR");
	}

	@Test
	@Ignore
	public void microsoftQuote() throws IOException, InterruptedException {
		assumeMicrosoft();
		test("a", "\"\\\"\"", "b");
	}

	@Ignore
	@Test
	public void posix() throws IOException, InterruptedException {
		assumePosix();
		test("a", "\"", "'", "${VAR}");
	}

	@Test
	public void simple() throws IOException, InterruptedException {
		test("argument");
	}

	protected void test(String... arguments) throws IOException, InterruptedException {
		final CommandInvocation.CommandInvocationBuilder<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocationBuilder = CommandInvocation.builder();
		invocationBuilder.format(ICommandFormat.getDefault());
		invocationBuilder.argument(HPlatform.getPlatform().getCategory().convertExecutablePathToString(getCliReport()));
		invocationBuilder.arguments(HCollection.asList(arguments));
		invocationBuilder.io(new StandardIO<>(ProcessBuilder.Redirect.INHERIT, ProcessBuilder.Redirect.PIPE, ProcessBuilder.Redirect.DISCARD));
		invocationBuilder.working(Paths.get(System.getProperty("user.dir")));
		invocationBuilder.environment(SystemEnvironment.create());
		final CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocation = invocationBuilder.build();
		final CommandInvocation<ProcessBuilder.Redirect, ProcessBuilder.Redirect> wrapped = ICommandRunner.create(null).wrap(invocation);
		final ProcessBuilder processBuilder = HProcess.createProcessBuilder(wrapped);

		final List<String> output = new ArrayList<>();
		final int exitCode;
		final Process process = processBuilder.start();
		try {
			final Thread thread = new Thread(() -> {
				HTextIO.readAll(process.getInputStream(), output::add);
			});
			thread.run();

			exitCode = process.waitFor();
			thread.interrupt();
		} finally {
			if (process != null) {
				process.descendants().forEach(handle -> handle.destroy());
				process.destroy();
				HIO.closeAll(process.getInputStream(), process.getErrorStream(), process.getOutputStream());
			}
		}
		HAssert.assertEquals(0, exitCode);

		final List<String> expected = HCollection.concatenate(HCollection.asList(String.format("CLIReport: %1$d arguments", invocation.getArguments().size())), invocation.getArguments().stream().map(argument -> String.format("%1$04d: %2$s", argument.length(), argument)).collect(Collectors.toList()));
		HAssert.assertEquals(expected, output);
	}
}
