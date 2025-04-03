package com.g2forge.alexandria.command.invocation.runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
	@Getter
	protected Path cliReport;

	@Test
	public void a() throws IOException, InterruptedException {
		test("a");
	}

	@Test
	public void ab() throws IOException, InterruptedException {
		test("A", "B");
	}

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
	public void exitCodeInvalid() throws IOException, InterruptedException {
		test("--exit", "x");
	}

	@Test
	public void exitCodeMissing() throws IOException, InterruptedException {
		test("--exit");
	}

	@Test
	public void exitCodeNotFirst() throws IOException, InterruptedException {
		test("a", "--exit", "1");
	}

	@Test
	public void exitCodeValid() throws IOException, InterruptedException {
		test("--exit", "1", "A", "B");
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

		final HCLIReport.Output expected = HCLIReport.computeExpectedOutput(invocation.getArguments());
		HAssert.assertEquals((byte) expected.getExitCode(), (byte) exitCode);
		HAssert.assertEquals(expected.getOutput(), output);
	}
}
