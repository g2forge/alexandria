package com.g2forge.alexandria.wizard;

import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public class CommandLineStringInput extends AInput<String> {
	protected final String[] arguments;

	protected final int index;

	@Override
	public String get() {
		if (isEmpty()) { throw new NoSuchElementException("There were too few command line arguments!"); }
		return getArguments()[getIndex()];
	}

	@Override
	public boolean isEmpty() {
		return getArguments().length <= getIndex();
	}
}
