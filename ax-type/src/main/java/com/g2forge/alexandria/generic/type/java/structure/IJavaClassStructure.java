package com.g2forge.alexandria.generic.type.java.structure;

import java.util.stream.Stream;

public interface IJavaClassStructure<E, C, F, M, S> extends IJavaTypeStructure<E> {
	public Stream<? extends S> getConstructors(JavaProtection minimum);

	public Stream<? extends F> getFields(JavaScope scope, JavaProtection minimum);

	public Stream<? extends C> getInterfaces();

	public Stream<? extends M> getMethods(JavaScope scope, JavaProtection minimum);

	public C getSuperClass();
}
