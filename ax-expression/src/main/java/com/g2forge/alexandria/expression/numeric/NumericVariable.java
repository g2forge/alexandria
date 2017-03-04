package com.g2forge.alexandria.expression.numeric;

import com.g2forge.alexandria.expression.IVariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumericVariable implements IVariable<NumericVariable, NumericEnvironment>, INumericExpression {
	protected String name;

	@Override
	public INumericExpression apply(NumericEnvironment environment) {
		return (environment == null) ? this : environment.lookup(this).or(this);
	}

	@Override
	public INumericExpression reduce() {
		return this;
	}

	@Override
	public String toString() {
		return getName();
	}
}
