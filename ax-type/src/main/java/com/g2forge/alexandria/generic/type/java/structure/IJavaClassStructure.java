package com.g2forge.alexandria.generic.type.java.structure;

import java.util.Collection;

public interface IJavaClassStructure<C, F> extends IJavaTypeStructure<C> {
	public Collection<? extends F> getFields(JavaMembership membership);
	
	public C getSuperClass();
}
