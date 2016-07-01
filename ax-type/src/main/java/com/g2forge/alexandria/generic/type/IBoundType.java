package com.g2forge.alexandria.generic.type;

import java.util.List;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IBoundType extends IResolvableType {
	@Override
	public IBoundType eval(ITypeEnvironment environment);
	
	public List<? extends IType> getActuals();
}
