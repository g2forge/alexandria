package com.g2forge.alexandria.command.invocation.runner;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.java.core.marker.ISingleton;

public class IdentityCommandRunner implements ICommandRunner, ISingleton {
	protected static final IdentityCommandRunner INSTANCE = new IdentityCommandRunner();

	public static IdentityCommandRunner create() {
		return INSTANCE;
	}

	private IdentityCommandRunner() {}

	@Override
	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation) {
		return invocation;
	}
}
