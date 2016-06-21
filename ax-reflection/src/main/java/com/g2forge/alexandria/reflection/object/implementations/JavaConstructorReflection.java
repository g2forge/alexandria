package com.g2forge.alexandria.reflection.object.implementations;

import com.g2forge.alexandria.generic.type.java.IJavaConstructorType;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaConstructorReflection;

public class JavaConstructorReflection<O> extends AJavaMemberReflection<O, IJavaConstructorType>implements IJavaConstructorReflection<O> {
	public JavaConstructorReflection(IJavaConstructorType type) {
		super(type);
	}
}
