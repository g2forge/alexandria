package com.g2forge.alexandria.reflection.object.implementations;

import com.g2forge.alexandria.generic.type.java.IJavaMethodType;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;

public class JavaMethodReflection<O, M> extends AJavaMemberReflection<O, M, IJavaMethodType>implements IJavaMethodReflection<O, M> {
	public JavaMethodReflection(IJavaMethodType type) {
		super(type);
	}
}
