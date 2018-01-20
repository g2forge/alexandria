package com.g2forge.alexandria.java.fluent;

import java.util.function.Function;

/**
 * Flag 0 indicates whether elements may be accessed, and how. Flag 1 indicates what changes in arity are allowed.
 */
public interface IFluent__<T> {
	public <U> IFluent__<U> map(Function<? super T, ? extends U> mapper);
}
