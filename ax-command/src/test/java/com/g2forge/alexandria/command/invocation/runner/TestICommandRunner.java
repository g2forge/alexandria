package com.g2forge.alexandria.command.invocation.runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.process.HProcess;
import com.g2forge.alexandria.command.stdio.StandardIO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.HTextIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.test.HAssert;

import lombok.Getter;

public class TestICommandRunner {
	protected static final String CLIREPORT_VERSION = "v0.0.1";

	protected static final String CLIREPORT_FILENAME = "clireport";

	@Getter
	protected Path cliReport;

	@Before
	public void before() {
		cliReport = Paths.get(HPlatform.getPlatform().getExeSpecs()[0].fromBase(CLIREPORT_FILENAME));
		if (!Files.exists(cliReport)) {
			try (final InputStream input = new URL(String.format("https://github.com/g2forge/clireport/releases/download/%1$s/%2$s", CLIREPORT_VERSION, cliReport.getFileName().toString())).openStream();
				final OutputStream output = Files.newOutputStream(cliReport)) {
				HBinaryIO.copy(input, output);
			} catch (IOException e) {
				throw new RuntimeIOException("Failed to download clireport", e);
			}
			HAssert.assertTrue(Files.exists(cliReport));
			try {
				Files.setPosixFilePermissions(cliReport, EnumSet.allOf(PosixFilePermission.class));
			} catch (UnsupportedOperationException e) {
				// Ignore this - it's not required on platforms where it's not supported
			} catch (IOException e) {
				throw new RuntimeIOException("Failed to mark clireport executable", e);
			}
			HAssert.assertTrue(Files.isExecutable(cliReport));
		}
	}

	@Test
	public void test() throws IOException, InterruptedException {
		final List<String> arguments = HCollection.asList(HPlatform.getPlatform().getCategory().convertExecutablePathToString(getCliReport()), "argument");
		final List<String> output = test(arguments);
		final List<String> expected = HCollection.concatenate(HCollection.asList(String.format("CLIReport: %1$d arguments", arguments.size())), arguments.stream().map(argument -> String.format("%1$04d: %2$s", argument.length(), argument)).collect(Collectors.toList()));
		HAssert.assertEquals(expected, output);
	}

	protected List<String> test(final List<String> arguments) throws IOException, InterruptedException {
		final CommandInvocation.CommandInvocationBuilder<ProcessBuilder.Redirect, ProcessBuilder.Redirect> invocationBuilder = CommandInvocation.builder();
		invocationBuilder.format(ICommandFormat.getDefault());
		invocationBuilder.arguments(arguments);
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
		return output;
	}
}
