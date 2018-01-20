package com.g2forge.alexandria.expression;

public interface IExpression<V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>> {
	public static <L extends ILiteral<V, N>, V extends IVariable<V, N>, N extends IEnvironment<V, N, ?>> L eval(IExpression<V, N> _this, Class<L> type) {
		if (type != null) {
			if (type.isInstance(_this)) return type.cast(_this);
		} else {
			if (_this instanceof ILiteral) {
				@SuppressWarnings("unchecked")
				final L cast = (L) _this;
				return cast;
			}
		}
		final IExpression<V, N> reduced = _this.reduce();
		if (reduced == _this) throw new ExpressionNotEvaluableException(_this, type);
		return reduced.eval(type);
	}

	/**
	 * Apply the given environment to this expression. May either be eager or lazy, at the implementors discretion.
	 * 
	 * @param environment The environment to apply to this expression.
	 * @return An expression equivalent to this one, with any variables present in the environment substituted for their values.
	 */
	public IExpression<V, N> apply(N environment);

	/**
	 * Evaluate this expression completely to a literal value.
	 * 
	 * @param <L> The (static) type of the literal result.
	 * @param type The type of the literal result to be expected (and required) from this evaluation.
	 * @return A literal expression resulting from the evaluation.
	 * @throws ExpressionNotEvaluableException if this expression cannot be completely evaluated.
	 */
	public default <L extends ILiteral<V, N>> L eval(Class<L> type) throws ExpressionNotEvaluableException {
		return eval(this, type);
	}

	/**
	 * Eagerly reduce this expression.
	 * 
	 * @return A reduced version of this expression.
	 */
	public IExpression<V, N> reduce();
}
