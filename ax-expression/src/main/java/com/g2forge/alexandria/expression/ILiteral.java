package com.g2forge.alexandria.expression;

public interface ILiteral<V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>> extends IExpression<V, N> {
	@Override
	public ILiteral<V, N> reduce();

	@Override
	public ILiteral<V, N> apply(N environment);
}
