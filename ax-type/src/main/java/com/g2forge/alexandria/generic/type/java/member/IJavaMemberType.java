package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Member;
import java.util.Set;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.structure.JavaModifier;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.java.adt.name.INamed;

public interface IJavaMemberType extends INamed<String>, IJavaUntype {
	@Override
	public IJavaMemberType eval(ITypeEnvironment environment);

	public IJavaClassType getDeclaringClass();

	public Member getJavaMember();

	public Set<JavaModifier> getModifiers();
}
