package com.g2forge.alexandria.expression.numeric;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.g2forge.alexandria.expression.ALazyExpression;
import com.g2forge.alexandria.expression.eval.IEvaluator;

public class LazyNumericExpression extends ALazyExpression<NumericVariable, NumericEnvironment, INumericExpression, INumericExpression>implements INumericExpression {
	public LazyNumericExpression(Supplier<? extends INumericExpression> input, Supplier<? extends NumericEnvironment> environment, BiFunction<? super INumericExpression, ? super NumericEnvironment, ? extends INumericExpression> function) {
		super(input, environment, function);
	}

	@Override
	public INumericExpression apply(NumericEnvironment environment) {
		return new LazyNumericExpression(getInput(), getEnvironment(), getFunction().andThen(e -> e.apply(environment)));
	}

	@Override
	protected IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> getEvaluator() {
		return EVAL.get();
	}

	@Override
	public INumericExpression reduce() {
		return resolve().reduce();
	}
}