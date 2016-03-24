package com.g2forge.alexandria.java.reflection;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JavaProtection {
	Private(Modifier.PRIVATE),
	Unspecified(0),
	Protected(Modifier.PROTECTED),
	Public(Modifier.PUBLIC);

	protected static final int MASK = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

	public static JavaProtection of(Member member) {
		final int modifier = member.getModifiers() & MASK;
		for (JavaProtection retVal : values()) {
			if (retVal.modifier == modifier) return retVal;
		}
		throw new IllegalArgumentException();
	}

	protected final int modifier;

	public boolean isAccessible(Member member) {
		if (modifier == 0) return (member.getModifiers() & MASK) == 0;
		return (member.getModifiers() & modifier) != 0;
	}
}