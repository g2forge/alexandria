package com.g2forge.alexandria.expression.numeric;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.expression.eval.EagerEvaluator;
import com.g2forge.alexandria.expression.eval.SimpleEvaluator;
import com.g2forge.alexandria.java.close.ICloseable;

public class TestNumericFunction {
	protected final NumericVariable a, b;

	protected final NumericFunction gcd;

	public TestNumericFunction() {
		a = new NumericVariable("a");
		b = new NumericVariable("b");
		gcd = new NumericFunction(a, b);
		gcd.setExpression(new ConditionalNumericExpression(b, gcd.apply(NumericEnvironment.FACTORY.build().add(a, b).add(b, new BinaryNumericExpression(BinaryNumericExpression.Operator.Mod, a, b)).build()), a));
	}

	@Test
	public void base() {
		for (boolean eager : new boolean[] { false, true }) {
			for (int i = 0; i < 5; i++) {
				Assert.assertEquals(i, gcd(i, 0, eager));
			}
		}
	}

	protected int gcd(final int _a, final int _b, boolean eager) {
		final ICloseable closeable = eager ? INumericExpression.EVAL.open(new EagerEvaluator<NumericVariable, NumericEnvironment, INumericExpression>(new SimpleEvaluator<>())) : null;
		try {
			final INumericExpression applied = gcd.apply(NumericEnvironment.FACTORY.build().add(a, new NumericLiteral(_a)).add(b, new NumericLiteral(_b)).build());
			Assert.assertFalse(applied instanceof NumericLiteral);
			return applied.eval(NumericLiteral.class).getValue();
		} finally {
			if (closeable != null) closeable.close();
		}
	}

	@Test
	public void misc() {
		for (boolean eager : new boolean[] { false, true }) {
			Assert.assertEquals(5, gcd(15, 25, eager));
			Assert.assertEquals(2, gcd(62, 36, eager));
		}
	}

	@Test
	public void once() {
		for (boolean eager : new boolean[] { false, true }) {
			for (int i = 0; i < 5; i++) {
				Assert.assertEquals(i, gcd(i, i, eager));
			}
		}
	}

	@Test
	public void prime() {
		for (boolean eager : new boolean[] { false, true }) {
			Assert.assertEquals(1, gcd(2, 3, eager));
			Assert.assertEquals(1, gcd(3, 5, eager));
			Assert.assertEquals(1, gcd(2, 1, eager));
		}
	}
}
