package com.g2forge.alexandria.expression.eval;

import java.util.function.Supplier;

import com.g2forge.alexandria.expression.ExpressionNotEvaluableException;
import com.g2forge.alexandria.expression.IEnvironment;
import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.IVariable;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IFunction2;

public interface IEvaluator<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> {
	public E apply(E expression, N environment, Supplier<E> supplier);

	public E apply(String description, E expression, N environment);

	public ICloseable debug();

	public <T> T eval(E expression, Class<T> type, Supplier<T> supplier) throws ExpressionNotEvaluableException;

	public <T> T eval(String description, E expression, Class<T> type, IFunction2<? super E, ? super Class<T>, ? extends T> function) throws ExpressionNotEvaluableException;

	public E reduce(E expression, Supplier<E> supplier);

	public E reduce(String description, E expression);
}
