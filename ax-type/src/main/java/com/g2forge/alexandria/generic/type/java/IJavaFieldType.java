package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;
import com.g2forge.alexandria.java.name.INamed;

public interface IJavaFieldType extends INamed<String>, IJavaTyped, IJavaUntype {
	public Field getJavaField();
}
