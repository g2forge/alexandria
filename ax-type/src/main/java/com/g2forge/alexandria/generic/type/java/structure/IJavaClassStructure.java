package com.g2forge.alexandria.generic.type.java.structure;

import java.util.stream.Stream;

public interface IJavaClassStructure<C, F, M, S> extends IJavaTypeStructure<C> {
	public Stream<? extends S> getConstructors(JavaProtection minimum);

	public Stream<? extends F> getFields(JavaScope scope, JavaProtection minimum);

	public Stream<? extends C> getInterfaces();

	public Stream<? extends M> getMethods(JavaScope scope, JavaProtection minimum);

	public C getSuperClass();
}
