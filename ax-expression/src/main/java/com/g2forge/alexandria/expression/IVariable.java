package com.g2forge.alexandria.expression;

public interface IVariable<V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>> extends IExpression<V, N> {}
