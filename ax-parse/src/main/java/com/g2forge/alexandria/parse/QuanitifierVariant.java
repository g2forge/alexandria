package com.g2forge.alexandria.parse;

public enum QuanitifierVariant {
	GREEDY,
	RELUCTANT,
	POSSESSIVE;

	public static final QuanitifierVariant DEFAULT = QuanitifierVariant.GREEDY;
}
