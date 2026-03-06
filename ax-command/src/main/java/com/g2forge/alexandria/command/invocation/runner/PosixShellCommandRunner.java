package com.g2forge.alexandria.command.invocation.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.format.BashCommandFormat;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.platform.Shell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PosixShellCommandRunner extends AShellCommandRunner {
	private final Shell shell;

	protected BashCommandFormat computeFormat() {
		final Shell shell = getShell();
		switch (shell) {
			case BASH:
				return BashCommandFormat.create();
			default:
				throw new EnumException(Shell.class, shell);
		}
	}

	@Override
	public <A, I, O> CommandInvocation<A, I, O> wrap(CommandInvocation<A, I, O> invocation) {
		final List<? extends String> shellArguments = getShellArguments();
		final List<String> retVal = new ArrayList<>(1 + shellArguments.size() + 1);
		retVal.add(getShellExecutable());
		retVal.addAll(shellArguments);
		retVal.add(invocation.getArguments().stream().map(invocation.getType()::get).collect(Collectors.joining(" ")));
		final BashCommandFormat format = computeFormat();
		return invocation.toBuilder().format(format).clearArguments().arguments(retVal.stream().map(invocation.getType()::create).toList()).build();
	}
}