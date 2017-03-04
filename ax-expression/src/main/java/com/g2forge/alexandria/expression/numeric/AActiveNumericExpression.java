package com.g2forge.alexandria.expression.numeric;

import java.util.function.Supplier;

import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.java.function.cache.FixedCachingSupplier;

public abstract class AActiveNumericExpression implements INumericExpression {
	protected final Supplier<INumericExpression> reduction = new FixedCachingSupplier<>(() -> {
		final IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator = EVAL.get();
		return evaluator.reduce(this, () -> reduceInternal(evaluator));
	});

	@Override
	public INumericExpression apply(NumericEnvironment environment) {
		final IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator = EVAL.get();
		return evaluator.apply(this, environment, () -> applyInternal(evaluator, environment));
	}

	protected abstract INumericExpression applyInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator, NumericEnvironment environment);

	@Override
	public INumericExpression reduce() {
		return reduction.get();
	}

	protected abstract INumericExpression reduceInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator);
}
