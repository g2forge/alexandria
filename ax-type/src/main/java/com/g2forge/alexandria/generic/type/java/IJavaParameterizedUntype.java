package com.g2forge.alexandria.generic.type.java;

import java.util.List;

import com.g2forge.alexandria.generic.type.IParameterizedType;
import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.type.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;

public interface IJavaParameterizedUntype extends IJavaUntype, IParameterizedType {
	@Override
	public IJavaBoundType bind(List<? extends IType> actuals);
	
	@Override
	public IJavaParameterizedUntype eval(ITypeEnvironment environment);
	
	@Override
	public List<? extends IJavaVariableType> getParameters();
}
