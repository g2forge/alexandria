package com.g2forge.alexandria.generic.type.java.type;

import java.util.List;

import com.g2forge.alexandria.generic.type.IBoundType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IJavaBoundType extends IJavaConcreteType, IBoundType {
	@Override
	public IJavaBoundType eval(ITypeEnvironment environment);

	@Override
	public List<? extends IJavaType> getActuals();

	public IJavaType getOwner();

	public IJavaType getRaw();
}
