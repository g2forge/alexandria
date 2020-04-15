package com.g2forge.alexandria.command.invocation.runner;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.format.NotExpressableCommandFormat;
import com.g2forge.alexandria.java.platform.Shell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CMDCommandRunner extends AShellCommandRunner {
	private final Shell shell;

	@Override
	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation) {
		final List<? extends String> shellArguments = getShellArguments();
		final List<String> retVal = new ArrayList<>(shellArguments.size() + 1 + invocation.getArguments().size());
		retVal.add(getShellExecutable());
		retVal.addAll(shellArguments);
		retVal.addAll(invocation.getArguments());
		return invocation.toBuilder().format(NotExpressableCommandFormat.create()).clearArguments().arguments(retVal).build();
	}
}