package com.g2forge.alexandria.command.command;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class DispatchCommand implements IStandardCommand {
	public interface IDispatchCommandBuilder extends IBuilder<DispatchCommand> {
		public default void main(String[] args) throws Throwable {
			IStandardCommand.main(args, build());
		}
	}

	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public static class ManualBuilder implements IDispatchCommandBuilder {
		protected final Map<String, IStandardCommand> commands = new HashMap<>();

		@Override
		public DispatchCommand build() {
			return new DispatchCommand(new HashMap<>(getCommands()));
		}

		public ManualBuilder command(IStandardCommand command, String... names) {
			for (String name : names) {
				commands.put(name, command);
			}
			return this;
		}
	}

	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public static class NamingBuilder<C extends IStandardCommand> implements IDispatchCommandBuilder {
		protected final IFunction1<? super C, ? extends String> namer;

		protected final Map<String, C> commands = new HashMap<>();

		@Override
		public DispatchCommand build() {
			return new DispatchCommand(new HashMap<>(getCommands()));
		}

		public NamingBuilder<C> command(C command) {
			commands.put(getNamer().apply(command), command);
			return this;
		}
	}

	@Singular
	protected final Map<String, IStandardCommand> commands;

	protected String getKnownSubCommands() {
		return getCommands().keySet().stream().map(s -> '"' + s + '"').collect(HCollector.joiningHuman());
	}

	@Override
	public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
		final List<String> arguments = invocation.getArguments();
		if (arguments.size() < 1) {
			invocation.getIo().getStandardError().println(String.format("No sub-command (or any arguments) were specified, known sub commands are: %1$s!", getKnownSubCommands()));
			return IStandardCommand.FAIL;
		}

		final String name = arguments.get(0);
		final IStandardCommand subcommand = getCommands().get(name);
		if (subcommand == null) {
			invocation.getIo().getStandardError().println(String.format("Unrecognized sub-command \"%1$s\", known sub commands are: %2$s!", name, getCommands().keySet().stream().map(s -> '"' + s + '"').collect(HCollector.joiningHuman())));
			return IStandardCommand.FAIL;
		}

		final CommandInvocation<InputStream, PrintStream> subinvocation = invocation.toBuilder().clearArguments().arguments(arguments.subList(1, arguments.size())).build();
		return subcommand.invoke(subinvocation);
	}

}
