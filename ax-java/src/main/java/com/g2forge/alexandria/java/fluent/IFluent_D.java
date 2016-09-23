package com.g2forge.alexandria.java.fluent;

import java.util.function.Predicate;

/**
 * "D" indicates that the arity can go down.
 * 
 * @author gdgib
 *
 * @param <T>
 */
public interface IFluent_D<T> extends IFluent__<T> {
	public IFluent__<T> filter(Predicate<? super T> predicate);
}
