package com.g2forge.alexandria.command.invocation.runner;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.platform.Shell;

public abstract class AShellCommandRunner implements ICommandRunner {
	public abstract Shell getShell();

	protected List<? extends String> getShellArguments() {
		return HCollection.asList(getShell().getArguments());
	}

	protected String getShellExecutable() {
		final Shell shell = getShell();
		return (shell.getName() == null) ? shell.name().toLowerCase() : shell.getName();
	}
}