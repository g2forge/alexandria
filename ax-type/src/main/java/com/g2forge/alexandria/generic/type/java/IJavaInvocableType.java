package com.g2forge.alexandria.generic.type.java;

import java.util.List;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.java.adt.name.INamed;

public interface IJavaInvocableType extends INamed<String>, IJavaUntype {
	public List<IJavaType> getParameterTypes();
}
