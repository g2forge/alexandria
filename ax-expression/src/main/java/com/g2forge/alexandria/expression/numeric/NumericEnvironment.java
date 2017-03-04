package com.g2forge.alexandria.expression.numeric;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.expression.IEnvironment;
import com.g2forge.alexandria.java.core.helpers.HObject;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NonNullOptional;
import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;
import com.g2forge.alexandria.java.fluent.optional.function.IOptionalFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NumericEnvironment implements IEnvironment<NumericVariable, NumericEnvironment, INumericExpression> {
	public static final IFactory<NumericVariable, NumericEnvironment, INumericExpression> FACTORY = new IFactory<NumericVariable, NumericEnvironment, INumericExpression>() {
		@Override
		public NumericEnvironment wrap(IOptionalFunction<NumericVariable, INumericExpression> function) {
			return new NumericEnvironment(function);
		}
	};

	protected final IOptionalFunction<NumericVariable, INumericExpression> function;

	@Override
	public Function<NumericVariable, INumericExpression> fallback(Function<? super NumericVariable, ? extends INumericExpression> fallback) {
		return function.fallback(fallback);
	}

	@Override
	public IOptional<INumericExpression> lookup(NumericVariable variable) {
		return NonNullOptional.FACTORY.upcast(function.apply(variable));
	}

	@Override
	public NumericEnvironment override(NumericEnvironment override) {
		return new NumericEnvironment(function.override(override.function));
	}

	@Override
	public NumericEnvironment recursive(BiPredicate<? super NumericVariable, ? super NumericVariable> terminate) {
		return recursive(terminate, true, NumericVariable.class);
	}

	@Override
	public NumericEnvironment recursive(BiPredicate<? super NumericVariable, ? super NumericVariable> terminate, boolean prior, Class<NumericVariable> type) {
		return new NumericEnvironment(function.recursive(terminate, prior, type));
	}

	@Override
	public NumericEnvironment restrict(Predicate<? super NumericVariable> predicate, IOptionalFactory factory) {
		if ((factory != null) && (factory != FACTORY.getOptionalFactory())) throw new IllegalArgumentException("Wrong optional factory was specified");
		return new NumericEnvironment(function.restrict(predicate, FACTORY.getOptionalFactory()));
	}

	@Override
	public String toString() {
		return HObject.toString(this, function);
	}
}
