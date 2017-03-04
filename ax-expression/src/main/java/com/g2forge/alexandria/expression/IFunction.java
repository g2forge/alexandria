package com.g2forge.alexandria.expression;

public interface IFunction<V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>> extends IExpression<V, N>, IParameterized<V> {}
