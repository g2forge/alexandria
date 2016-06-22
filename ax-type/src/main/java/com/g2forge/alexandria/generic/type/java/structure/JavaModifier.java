package com.g2forge.alexandria.generic.type.java.structure;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JavaModifier {
	Public(Modifier.PUBLIC),
	Private(Modifier.PRIVATE),
	Protected(Modifier.PROTECTED),
	Static(Modifier.STATIC),
	Final(Modifier.FINAL),
	Synchronized(Modifier.SYNCHRONIZED),
	Volatile(Modifier.VOLATILE),
	Transient(Modifier.TRANSIENT),
	Native(Modifier.NATIVE),
	Interface(Modifier.INTERFACE),
	Abstract(Modifier.ABSTRACT),
	Strict(Modifier.STRICT);

	public static Set<JavaModifier> of(Member member) {
		final EnumSet<JavaModifier> retVal = EnumSet.noneOf(JavaModifier.class);
		final int modifiers = member.getModifiers();
		for (JavaModifier modifier : values()) {
			if ((modifier.modifier & modifiers) != 0) retVal.add(modifier);
		}
		return retVal;
	}

	protected final int modifier;
}