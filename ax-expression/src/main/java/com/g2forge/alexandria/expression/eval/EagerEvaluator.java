package com.g2forge.alexandria.expression.eval;

import java.util.function.Supplier;

import com.g2forge.alexandria.expression.ExpressionNotEvaluableException;
import com.g2forge.alexandria.expression.IEnvironment;
import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.ILazyExpression;
import com.g2forge.alexandria.expression.IVariable;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.resource.FlagResource;

import lombok.Data;

@Data
public class EagerEvaluator<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> implements IEvaluator<V, N, E> {
	protected final FlagResource lazy = new FlagResource();

	protected final IEvaluator<V, N, E> nested;

	@Override
	public E apply(E expression, N environment, Supplier<E> supplier) {
		final E applied = getNested().apply(expression, environment, supplier);
		return reduceAfterApply(applied);
	}

	@Override
	public E apply(String description, E expression, N environment) {
		final E applied = getNested().apply(description, expression, environment);
		return reduceAfterApply(applied);
	}

	@Override
	public ICloseable debug() {
		return getNested().debug();
	}

	@Override
	public <T> T eval(E expression, Class<T> type, Supplier<T> supplier) throws ExpressionNotEvaluableException {
		return getNested().eval(expression, type, supplier);
	}

	@Override
	public <T> T eval(String description, E expression, Class<T> type, IFunction2<? super E, ? super Class<T>, ? extends T> function) throws ExpressionNotEvaluableException {
		return getNested().eval(description, expression, type, function);
	}

	@Override
	public E reduce(E expression, Supplier<E> supplier) {
		if (lazy.get() && (expression instanceof ILazyExpression)) return expression;
		return getNested().reduce(expression, supplier);
	}

	@Override
	public E reduce(String description, E expression) {
		if (lazy.get() && (expression instanceof ILazyExpression)) return expression;
		return getNested().reduce(description, expression);
	}

	protected E reduceAfterApply(final E applied) {
		if (applied instanceof ILazyExpression) return applied;
		return getNested().reduce(applied, () -> {
			try (final ICloseable lazy = this.lazy.open(true)) {
				@SuppressWarnings("unchecked")
				final E cast = (E) applied.reduce();
				return cast;
			}
		});
	}
}
