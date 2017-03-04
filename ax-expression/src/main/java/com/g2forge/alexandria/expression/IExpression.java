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
	 * @param environment
	 * @return
	 */
	public IExpression<V, N> apply(N environment);

	/**
	 * Evaluate this expression completely to a literal value.
	 * 
	 * @param environment
	 * @return
	 * @throws ExpressionNotEvaluableException
	 *             if this expression cannot be completely evaluated.
	 */
	public default <L extends ILiteral<V, N>> L eval(Class<L> type) throws ExpressionNotEvaluableException {
		return eval(this, type);
	}

	/**
	 * Eagerly reduce this expression.
	 * 
	 * @param environment
	 * @return
	 */
	public IExpression<V, N> reduce();
}
