package com.g2forge.alexandria.command;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.java.core.iface.ICommand;

public interface IStructuredCommand extends ICommand {
	public static class SubCommandBuilder {
		protected final Map<String, IStandardCommand> map = new HashMap<>();

		public SubCommandBuilder add(IStandardCommand subcommand, String... names) {
			for (String name : names) {
				if (map.put(name, subcommand) != null) throw new IllegalArgumentException(String.format("Command named \"%1$s\" was already defined!", name));
			}
			return this;
		}

		public IStandardCommand build() {
			return invocation -> {
				final List<String> arguments = invocation.getArguments();

				final String name = arguments.get(0);
				final IStandardCommand subcommand = map.get(name);
				if (subcommand == null) {
					invocation.io.getStandardError().println(String.format("Unrecognized command \"%1$s\"!", name));
					return ICommand.FAIL;
				} else {
					final Invocation<InputStream, PrintStream> subinvocation = new Invocation<>(arguments.subList(1, arguments.size()), invocation.getIo(), invocation.getWorking());
					return subcommand.invoke(subinvocation);
				}
			};
		}
	}

	public static void main(String[] args, IStandardCommand command) throws Throwable {
		final Invocation<InputStream, PrintStream> invocation = Invocation.of(args);
		final int exitCode = command.invoke(invocation);
		System.exit(exitCode);
	}
}
