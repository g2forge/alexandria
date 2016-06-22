package com.g2forge.alexandria.generic.type.java.structure;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public enum JavaProtection {
	Private(JavaModifier.Private),
	Unspecified(null),
	Protected(JavaModifier.Protected),
	Public(JavaModifier.Public);

	protected static final int MASK = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

	public static JavaProtection of(Member member) {
		final int modifier = member.getModifiers() & MASK;
		for (JavaProtection retVal : values()) {
			if (retVal.modifier == modifier) return retVal;
		}
		throw new IllegalArgumentException();
	}

	protected final int modifier;

	private JavaProtection(JavaModifier modifier) {
		this.modifier = modifier == null ? 0 : modifier.modifier;
	}

	public boolean isAccessible(Member member) {
		if (modifier == 0) return (member.getModifiers() & MASK) == 0;
		return (member.getModifiers() & modifier) != 0;
	}
}