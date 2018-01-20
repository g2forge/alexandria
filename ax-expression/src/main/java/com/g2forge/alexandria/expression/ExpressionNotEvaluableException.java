package com.g2forge.alexandria.expression;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpressionNotEvaluableException extends RuntimeException {
	private static final long serialVersionUID = -7958704815301247691L;

	protected final IExpression<?, ?> expression;

	protected final Class<? extends IExpression<?, ?>> type;
}
