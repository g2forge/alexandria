package com.g2forge.alexandria.command.command;

import java.io.InputStream;
import java.io.PrintStream;

import com.g2forge.alexandria.annotations.service.Service;
import com.g2forge.alexandria.command.exit.Exit;
import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.invocation.CommandInvocation;

@Command
@Service(IAnnotatedTestCommand.class)
public class AnnotatedTestCommand1 implements IAnnotatedTestCommand {
	@Override
	public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
		return new Exit(1);
	}
}
