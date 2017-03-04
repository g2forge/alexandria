package com.g2forge.alexandria.expression;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;
import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFunctionFactory;
import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunctional;

public interface IEnvironment<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> extends IOptionalFunctional<V, E, N> {
	public static interface IFactory<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> extends IOptionalFunctionFactory<V, E, N> {}

	public IOptional<? extends IExpression<V, N>> lookup(V variable);

	public N override(N override);

	public N recursive(BiPredicate<? super V, ? super V> terminate);

	@Override
	public N recursive(BiPredicate<? super V, ? super V> terminate, boolean prior, Class<V> type);

	public N restrict(Predicate<? super V> predicate, IOptionalFactory factory);
}
