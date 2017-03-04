package com.g2forge.alexandria.expression.numeric;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.expression.numeric.BinaryNumericExpression;
import com.g2forge.alexandria.expression.numeric.INumericExpression;
import com.g2forge.alexandria.expression.numeric.NumericEnvironment;
import com.g2forge.alexandria.expression.numeric.NumericLiteral;
import com.g2forge.alexandria.expression.numeric.NumericVariable;

public class TestNumericVariable {
	@Test
	public void basic() {
		final NumericVariable x = new NumericVariable("x");
		final INumericExpression expression = new BinaryNumericExpression(BinaryNumericExpression.Operator.Add, x, new NumericLiteral(1));
		final NumericEnvironment environment = NumericEnvironment.FACTORY.of(x, new NumericLiteral(2));
		Assert.assertEquals(new NumericLiteral(3), expression.apply(environment).eval(null));
	}
}
