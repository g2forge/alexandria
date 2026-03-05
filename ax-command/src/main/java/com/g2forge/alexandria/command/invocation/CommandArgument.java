package com.g2forge.alexandria.command.invocation;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CommandArgument<A> {
	protected final ICommandArgumentType<A> type;

	protected final Path working;

	protected final A value;

	public CommandArgument<A> withValue(String string) {
		return toBuilder().value(getType().create(string)).build();
	}

	public Path getPath() {
		final String string = getString();
		if (string == null) return null;

		final Path path = Paths.get(string);
		if (path.isAbsolute()) return path;
		final Path working = getWorking();
		if (working == null) return path;
		return working.resolve(path);
	}

	public String getString() {
		final A argument = getValue();
		if (argument == null) return null;
		return getType().get(argument);
	}
}
