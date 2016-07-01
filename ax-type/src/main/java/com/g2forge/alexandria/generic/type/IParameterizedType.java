package com.g2forge.alexandria.generic.type;

import java.util.List;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IParameterizedType extends IType {
	public IBoundType bind(List<? extends IType> actuals);
	
	@Override
	public IParameterizedType eval(ITypeEnvironment environment);
	
	public List<? extends IVariableType> getParameters();
}
