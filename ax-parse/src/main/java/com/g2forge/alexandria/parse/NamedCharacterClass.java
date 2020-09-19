package com.g2forge.alexandria.parse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NamedCharacterClass {
	Any("."),
	Space;

	protected final String regex;

	private NamedCharacterClass() {
		this(null);
	}

	public String getRegex() {
		if (regex == null) return "\\p{" + name() + "}";
		return regex;
	}
}
