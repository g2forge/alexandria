package com.g2forge.alexandria.generic.type.java.structure;

import java.util.stream.Stream;

public interface IJavaClassStructure<C, F, M> extends IJavaTypeStructure<C> {
	public Stream<? extends F> getFields(JavaScope scope, JavaProtection minimum);

	public Stream<? extends M> getMethods(JavaScope scope, JavaProtection minimum);

	public C getSuperClass();
}
