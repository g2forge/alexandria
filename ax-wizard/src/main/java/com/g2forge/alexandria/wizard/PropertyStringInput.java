package com.g2forge.alexandria.wizard;

import java.util.NoSuchElementException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PropertyStringInput extends AInput<String> {
	@Getter
	protected final String property;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final String value = System.getProperty(getProperty());

	@Override
	public String get() {
		if (isEmpty()) { throw new NoSuchElementException(String.format("The property \"%1$s\" was not set!", getProperty())); }
		return getValue();
	}

	@Override
	public boolean isEmpty() {
		return getValue() == null;
	}
}
