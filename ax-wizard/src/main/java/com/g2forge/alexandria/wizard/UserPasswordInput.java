package com.g2forge.alexandria.wizard;

import java.io.Console;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserPasswordInput extends AInput<String> {
	protected static final Console console = System.console();

	@Getter
	protected final String prompt;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final String value = computeValue();

	protected String computeValue() {
		if (console == null) return UserStringInput.prompt(getPrompt(), null, new String[] { getPrompt() }, new boolean[] { false })[0];
		return new String(console.readPassword("%s: ", getPrompt()));
	}

	@Override
	public String get() {
		return getValue();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
