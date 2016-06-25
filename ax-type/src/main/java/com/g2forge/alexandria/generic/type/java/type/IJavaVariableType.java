package com.g2forge.alexandria.generic.type.java.type;

import java.util.Collection;

import com.g2forge.alexandria.generic.type.IVariableType;

public interface IJavaVariableType extends IVariableType, IJavaType {
	public Collection<? extends IJavaType> getUpperBounds();
}
