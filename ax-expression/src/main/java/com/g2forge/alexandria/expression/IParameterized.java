package com.g2forge.alexandria.expression;

import java.util.List;

public interface IParameterized<V extends IVariable<V, ?>> {
	public List<? extends V> getParameters();
}
