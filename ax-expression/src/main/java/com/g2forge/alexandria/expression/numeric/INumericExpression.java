package com.g2forge.alexandria.expression.numeric;

import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.ILiteral;
import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.expression.eval.SimpleEvaluator;
import com.g2forge.alexandria.java.nestedstate.INestedState;
import com.g2forge.alexandria.java.nestedstate.StackThreadState;

public interface INumericExpression extends IExpression<NumericVariable, NumericEnvironment> {
	public static final INestedState<IEvaluator<NumericVariable, NumericEnvironment, INumericExpression>> EVAL = new StackThreadState<>(new SimpleEvaluator<>());

	@Override
	public INumericExpression apply(NumericEnvironment environment);

	@Override
	public default <L extends ILiteral<NumericVariable, NumericEnvironment>> L eval(Class<L> type) {
		return EVAL.get().eval(this, type, () -> IExpression.eval(this, type));
	}

	@Override
	public INumericExpression reduce();
}
