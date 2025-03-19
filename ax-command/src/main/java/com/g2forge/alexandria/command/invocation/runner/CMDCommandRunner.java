package com.g2forge.alexandria.command.invocation.runner;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.command.AXCommandFlag;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.command.invocation.format.ICommandFormat;
import com.g2forge.alexandria.command.invocation.format.NotExpressableCommandFormat;
import com.g2forge.alexandria.command.invocation.format.PassthroughCommandFormat;
import com.g2forge.alexandria.java.platform.Shell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CMDCommandRunner extends AShellCommandRunner {
	@Getter(lazy = true)
	private static final Boolean passthrough = AXCommandFlag.FORCE_CMD_PASSTHROUGH.getAccessor().get();

	private final Shell shell;

	@Override
	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation) {
		final List<? extends String> shellArguments = getShellArguments();
		final List<String> retVal = new ArrayList<>(1 + shellArguments.size() + invocation.getArguments().size());
		retVal.add(getShellExecutable());
		retVal.addAll(shellArguments);
		retVal.addAll(invocation.getArguments());
		final ICommandFormat format = CMDCommandRunner.getPassthrough() ? PassthroughCommandFormat.create() : NotExpressableCommandFormat.create();
		return invocation.toBuilder().format(format).clearArguments().arguments(retVal).build();
	}
}