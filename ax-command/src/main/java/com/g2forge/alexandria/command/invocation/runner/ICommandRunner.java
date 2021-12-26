package com.g2forge.alexandria.command.invocation.runner;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;
import com.g2forge.alexandria.java.platform.Shell;

@FunctionalInterface
public interface ICommandRunner {
	public static ICommandRunner create(Shell shell) {
		final Shell actualShell = (shell == null) ? HPlatform.getPlatform().getShell() : shell;
		switch (actualShell.getCategory()) {
			case Posix:
				if (shell == null) return IdentityCommandRunner.create();
				return new PosixShellCommandRunner(actualShell);
			case Microsoft:
				switch (actualShell) {
					case CMD:
						return new CMDCommandRunner(actualShell);
					default:
						throw new EnumException(Shell.class, actualShell);
				}
			default:
				throw new EnumException(PlatformCategory.class, actualShell.getCategory());
		}
	}

	public <I, O> CommandInvocation<I, O> wrap(CommandInvocation<I, O> invocation);
}
