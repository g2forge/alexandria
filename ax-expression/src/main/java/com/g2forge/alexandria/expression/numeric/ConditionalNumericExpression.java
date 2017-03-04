package com.g2forge.alexandria.expression.numeric;

import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConditionalNumericExpression extends AActiveNumericExpression {
	protected final INumericExpression condition;

	protected final INumericExpression valueNonZero;

	protected final INumericExpression valueZero;

	@Override
	protected INumericExpression applyInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator, NumericEnvironment environment) {
		final INumericExpression condition = evaluator.apply("Condition", getCondition(), environment);
		final INumericExpression valueNonZero = evaluator.apply("NonZero", getValueNonZero(), environment);
		final INumericExpression valueZero = evaluator.apply("Zero", getValueZero(), environment);
		return new ConditionalNumericExpression(condition, valueNonZero, valueZero);
	}

	@Override
	protected INumericExpression reduceInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator) {
		final INumericExpression condition = evaluator.reduce("Condition", getCondition()), retVal;
		if (condition instanceof NumericLiteral) {
			final boolean conditionAsBoolean = ((NumericLiteral) condition).getValue() != 0;
			retVal = conditionAsBoolean ? evaluator.reduce("NonZero", getValueNonZero()) : evaluator.reduce("Zero", getValueZero());
		} else {
			final INumericExpression nonZero = evaluator.reduce("NonZero", getValueNonZero());
			final INumericExpression zero = evaluator.reduce("Zero", getValueZero());
			retVal = new ConditionalNumericExpression(condition, nonZero, zero);
		}
		return retVal;
	}

	@Override
	public String toString() {
		try (final ICloseable debug = EVAL.get().debug()) {
			return HObject.toString(this, ConditionalNumericExpression::getCondition, ConditionalNumericExpression::getValueNonZero, ConditionalNumericExpression::getValueZero);
		}
	}
}
