package com.g2forge.alexandria.command.invocation.runner;

import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;
import com.g2forge.alexandria.java.platform.Shell;

@FunctionalInterface
public interface ICommandRunner {
	/**
	 * Create a command runner for the local machine. This method will take into account the current platform (OS) and JVM and compensate for any quirks in
	 * might introduce.
	 * 
	 * @return A command runner for the local machine which does nothing to wrap the commands, other than compensate for platform issues.
	 */
	public static ICommandRunner create() {
		final PlatformCategory category = HPlatform.getPlatform().getCategory();
		switch (category) {
			case Posix:
				return PosixPathCommandRunner.create();
			default:
				return IdentityCommandRunner.create();
		}
	}

	public static ICommandRunner create(Shell shell) {
		final Shell platformShell = (shell == null) ? HPlatform.getPlatform().getShell() : shell;
		switch (platformShell.getCategory()) {
			case Posix:
				if (shell == null) return ICommandRunner.create();
				return new PosixShellCommandRunner(platformShell);
			case Microsoft:
				switch (platformShell) {
					case CMD:
						return new CMDCommandRunner(platformShell);
					default:
						throw new EnumException(Shell.class, platformShell);
				}
			default:
				throw new EnumException(PlatformCategory.class, platformShell.getCategory());
		}
	}

	public <A, I, O> CommandInvocation<A, I, O> wrap(CommandInvocation<A, I, O> invocation);
}
