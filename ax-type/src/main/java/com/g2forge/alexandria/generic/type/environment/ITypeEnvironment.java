package com.g2forge.alexandria.generic.type.environment;

import java.util.function.Function;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.IVariableType;

@FunctionalInterface
public interface ITypeEnvironment extends Function<IVariableType, IType> {}
