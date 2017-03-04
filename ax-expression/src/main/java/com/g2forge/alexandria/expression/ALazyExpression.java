package com.g2forge.alexandria.expression;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Getter(AccessLevel.PROTECTED)
public abstract class ALazyExpression<V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>, I, O> implements ILazyExpression<V, N> {
	protected final Supplier<? extends I> input;

	protected final Supplier<? extends N> environment;

	protected final BiFunction<? super I, ? super N, ? extends O> function;

	protected abstract IEvaluator<V, N, ?> getEvaluator();

	protected O resolve() {
		final I input = getInput().get();
		final N environment = getEnvironment().get();
		final O retVal = getFunction().apply(input, environment);
		return retVal;
	}

	@Override
	public String toString() {
		try (final ICloseable debugging = getEvaluator().debug()) {
			return HObject.toString(this, b -> b.append("input=").append(getInput()).append(", environment=").append(getEnvironment().get()).append(", function=").append(getFunction()));
		}
	}
}