package com.g2forge.alexandria.generic.type.java;

import java.util.Collection;

import com.g2forge.alexandria.generic.type.IBoundType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IJavaBoundType extends IJavaType, IBoundType {
	@Override
	public IJavaBoundType eval(ITypeEnvironment environment);
	
	@Override
	public Collection<? extends IJavaType> getActuals();
	
	public IJavaType getOwner();
	
	public IJavaType getRaw();
}
