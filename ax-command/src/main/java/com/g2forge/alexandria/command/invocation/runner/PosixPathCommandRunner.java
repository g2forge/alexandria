package com.g2forge.alexandria.command.invocation.runner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.platform.HPlatform;

public class PosixPathCommandRunner implements ICommandRunner, ISingleton {
	protected static final PosixPathCommandRunner INSTANCE = new PosixPathCommandRunner();

	public static PosixPathCommandRunner create() {
		return INSTANCE;
	}

	private PosixPathCommandRunner() {}

	@Override
	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation) {
		final String pathAsString = invocation.getEnvironment().apply(HPlatform.PATH);
		// If the invocation PATH and system PATH are the same, then we can delegate to the underlying JVM code
		if (Objects.equals(SystemEnvironment.create().apply(HPlatform.PATH), pathAsString)) return invocation;

		// Since the user is overriding the PATH, let's search that PATH for the executable
		// We have to do this here because the JVM doesn't allow us to do this down at the process builder level
		final String[] pathAsArray = HPlatform.getPlatform().getPathSpec().splitPaths(pathAsString);
		for (String directory : pathAsArray) {
			final Path resolved = Paths.get(directory).resolve(invocation.getArguments().get(0));
			if (Files.exists(resolved) && Files.isExecutable(resolved)) {
				final List<String> arguments = new ArrayList<>(invocation.getArguments());
				arguments.set(0, resolved.toString());
				return invocation.toBuilder().clearArguments().arguments(arguments).build();
			}
		}
		return invocation;
	}
}
