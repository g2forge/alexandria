package com.g2forge.alexandria.reflection.object.implementations;

import com.g2forge.alexandria.generic.type.java.IJavaMethodType;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;

public class JavaMethodReflection<O> extends AJavaMemberReflection<O, IJavaMethodType>implements IJavaMethodReflection<O> {
	public JavaMethodReflection(IJavaMethodType type) {
		super(type);
	}
}
