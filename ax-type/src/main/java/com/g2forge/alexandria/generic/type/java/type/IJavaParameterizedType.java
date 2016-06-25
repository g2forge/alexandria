package com.g2forge.alexandria.generic.type.java.type;

import java.util.List;

import com.g2forge.alexandria.generic.type.IParameterizedType;
import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IJavaParameterizedType extends IParameterizedType {
	@Override
	public IJavaBoundType bind(List<? extends IType> actuals);
	
	@Override
	public IJavaParameterizedType eval(ITypeEnvironment environment);
	
	@Override
	public List<? extends IJavaVariableType> getParameters();
}
