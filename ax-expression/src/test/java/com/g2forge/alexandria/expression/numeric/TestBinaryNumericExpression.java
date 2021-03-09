package com.g2forge.alexandria.expression.numeric;

import org.junit.Assert;
import org.junit.Test;

public class TestBinaryNumericExpression {
	@Test
	public void reduceAdd() {
		final NumericVariable x = new NumericVariable("x");
		final INumericExpression actual = new BinaryNumericExpression(BinaryNumericExpression.Operator.Add, new NumericLiteral(-3), x, new NumericLiteral(1)).reduce();
		final INumericExpression expected = new BinaryNumericExpression(BinaryNumericExpression.Operator.Add, x, new NumericLiteral(-2));
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void reduceSubtract() {
		final NumericVariable x = new NumericVariable("x"), y = new NumericVariable("y");
		final INumericExpression actual = new BinaryNumericExpression(BinaryNumericExpression.Operator.Subtract, x, new NumericLiteral(1), new NumericLiteral(-3), y).reduce();
		final INumericExpression expected = new BinaryNumericExpression(BinaryNumericExpression.Operator.Subtract, x, new BinaryNumericExpression(BinaryNumericExpression.Operator.Add, y, new NumericLiteral(-2)));
		Assert.assertEquals(expected, actual);
	}
}
