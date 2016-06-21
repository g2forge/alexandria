package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Member;
import java.util.Set;

import com.g2forge.alexandria.generic.type.java.structure.JavaModifier;
import com.g2forge.alexandria.java.name.INamed;

public interface IJavaMemberType extends INamed<String>, IJavaUntype {
	public IJavaClassType getDeclaringClass();

	public Member getJavaMember();

	public Set<JavaModifier> getModifiers();
}
