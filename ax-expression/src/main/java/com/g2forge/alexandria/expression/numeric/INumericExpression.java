package com.g2forge.alexandria.expression.numeric;

import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.ILiteral;
import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.expression.eval.SimpleEvaluator;
import com.g2forge.alexandria.java.resource.ICloseableResource;
import com.g2forge.alexandria.java.resource.StackThreadResource;

public interface INumericExpression extends IExpression<NumericVariable, NumericEnvironment> {
	public static final ICloseableResource<IEvaluator<NumericVariable, NumericEnvironment, INumericExpression>> EVAL = new StackThreadResource<>(new SimpleEvaluator<>());

	@Override
	public INumericExpression apply(NumericEnvironment environment);

	@Override
	public default <L extends ILiteral<NumericVariable, NumericEnvironment>> L eval(Class<L> type) {
		return EVAL.get().eval(this, type, () -> IExpression.eval(this, type));
	}

	@Override
	public INumericExpression reduce();
}
