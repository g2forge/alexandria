package com.g2forge.alexandria.expression.eval;

import java.util.function.Supplier;

import com.g2forge.alexandria.expression.ExpressionNotEvaluableException;
import com.g2forge.alexandria.expression.IEnvironment;
import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.IVariable;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IFunction2;

public class SimpleEvaluator<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> implements IEvaluator<V, N, E> {
	@Override
	public E apply(E expression, N environment, Supplier<E> supplier) {
		return supplier.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E apply(String description, E expression, N environment) {
		if (expression == null) { throw new NullPointerException(); }
		return (E) expression.apply(environment);
	}

	@Override
	public ICloseable debug() {
		return () -> {};
	}

	@Override
	public <T> T eval(E expression, Class<T> type, Supplier<T> supplier) throws ExpressionNotEvaluableException {
		return supplier.get();
	}

	@Override
	public <T> T eval(String description, E expression, Class<T> type, IFunction2<? super E, ? super Class<T>, ? extends T> function) throws ExpressionNotEvaluableException {
		return function.apply(expression, type);
	}

	@Override
	public E reduce(E expression, Supplier<E> supplier) {
		return supplier.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E reduce(String description, E expression) {
		return (E) expression.reduce();
	}
}
