package com.g2forge.alexandria.expression.numeric;

import com.g2forge.alexandria.expression.ILiteral;

import lombok.Data;

@Data
public class NumericLiteral implements ILiteral<NumericVariable, NumericEnvironment>, INumericExpression {
	protected final int value;

	@Override
	public NumericLiteral apply(NumericEnvironment environment) {
		return this;
	}

	@Override
	public NumericLiteral reduce() {
		return this;
	}

	@Override
	public String toString() {
		return Integer.toString(getValue());
	}
}
