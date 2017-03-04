package com.g2forge.alexandria.expression.numeric;

import java.util.Arrays;
import java.util.List;

import com.g2forge.alexandria.expression.IFunction;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class NumericFunction implements IFunction<NumericVariable, NumericEnvironment>, INumericExpression {
	protected INumericExpression expression;

	protected final List<NumericVariable> parameters;

	public NumericFunction(NumericVariable... parameters) {
		this(Arrays.asList(parameters));
	}

	@Override
	public INumericExpression apply(NumericEnvironment environment) {
		return new LazyNumericExpression(this::getExpressionNonNull, () -> environment.restrict(getParameters()::contains, null), INumericExpression::apply);
	}

	protected INumericExpression getExpressionNonNull() {
		final INumericExpression expression = getExpression();
		if (expression == null) throw new NullPointerException("Expression was not initialized");
		return expression;
	}

	@Override
	public INumericExpression reduce() {
		final INumericExpression expression = getExpressionNonNull(), reduced = expression.reduce();
		if (expression != reduced) return new NumericFunction(getParameters()).setExpression(reduced);;
		return this;
	}
}
