package com.g2forge.alexandria.wizard;

import java.util.NoSuchElementException;

public class InputUnspecifiedException extends NoSuchElementException {
	private static final long serialVersionUID = -5902244684430582312L;

	public InputUnspecifiedException() {}

	public InputUnspecifiedException(String message) {
		super(message);
	}
}
