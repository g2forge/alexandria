package com.g2forge.alexandria.generic.type.java.type;

import java.util.Collection;

import com.g2forge.alexandria.generic.type.IVariableType;

public interface IJavaVariableType extends IJavaType, IVariableType {
	public Collection<? extends IJavaType> getUpperBounds();
}
