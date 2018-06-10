package com.g2forge.alexandria.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.java.core.iface.ICommand;

public interface IStructuredCommand extends ICommand {
	public static class ISubCommandBuilder {
		protected final Map<String, IStandardCommand> map = new HashMap<>();

		public ISubCommandBuilder add(IStandardCommand subcommand, String... names) {
			for (String name : names) {
				if (map.put(name, subcommand) != null) throw new IllegalArgumentException(String.format("Command named \"%1$s\" was already defined!", name));
			}
			return this;
		}

		public IStandardCommand build() {
			return invocation -> {
				final List<String> arguments = invocation.getArguments();
				final IStandardCommand subcommand = map.get(arguments.get(0));
				final CommandInvocation subinvocation = new CommandInvocation(arguments.subList(1, arguments.size()));
				return subcommand.invoke(subinvocation);
			};
		}
	}

	public static void main(String[] args, IStandardCommand command) throws Throwable {
		final CommandInvocation invocation = CommandInvocation.of(args);
		final int exitCode = command.invoke(invocation);
		System.exit(exitCode);
	}
}
