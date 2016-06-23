package com.g2forge.alexandria.generic.type.java;

import java.util.Collection;

import com.g2forge.alexandria.generic.type.ITypeVariable;

public interface IJavaVariableType extends ITypeVariable, IJavaType {
	public Collection<? extends IJavaType> getUpperBounds();
}
