package com.g2forge.alexandria.generic.type.java.structure;

import java.util.stream.Stream;

import com.g2forge.alexandria.java.reflect.JavaProtection;
import com.g2forge.alexandria.java.reflect.JavaScope;

/**
 * @param <R>
 *            Raw types the result of {@link IJavaTypeStructure#erase()}
 * @param <T>
 *            Complete types
 * @param <F>
 *            Field
 * @param <M>
 *            Method
 * @param <C>
 *            Constructor
 */
public interface IJavaClassStructure<R, T, F, M, C> extends IJavaTypeStructure<R> {
	public Stream<? extends C> getConstructors(JavaProtection minimum);

	public Stream<? extends F> getFields(JavaScope scope, JavaProtection minimum);

	public Stream<? extends T> getInterfaces();

	public Stream<? extends M> getMethods(JavaScope scope, JavaProtection minimum);

	public T getParent(T parent);

	public T getSuperClass();

	public boolean isEnum();
}
