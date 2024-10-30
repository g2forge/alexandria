package com.g2forge.alexandria.command.invocation.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.format.BashCommandFormat;
import com.g2forge.alexandria.java.platform.Shell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PosixShellCommandRunner extends AShellCommandRunner {
	private final Shell shell;

	@Override
	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation) {
		final List<? extends String> shellArguments = getShellArguments();
		final List<String> retVal = new ArrayList<>(1 + shellArguments.size() + 1);
		retVal.add(getShellExecutable());
		retVal.addAll(shellArguments);
		retVal.add(invocation.getArguments().stream().collect(Collectors.joining(" ")));
		return invocation.toBuilder().format(BashCommandFormat.create()).clearArguments().arguments(retVal).build();
	}
}