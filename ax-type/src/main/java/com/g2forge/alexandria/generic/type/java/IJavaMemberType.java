package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Member;

import com.g2forge.alexandria.java.name.INamed;

public interface IJavaMemberType extends INamed<String>, IJavaUntype {
	public Member getJavaMember();

	public IJavaClassType getDeclaringClass();
}
