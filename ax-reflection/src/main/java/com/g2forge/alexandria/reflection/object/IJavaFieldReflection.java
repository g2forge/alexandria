package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;

public interface IJavaFieldReflection<T, F> extends IJavaMemberReflection<T>, IJavaGenericTyped<F> {
	public ITuple1GS<F> getAccessor(T object);

	@Override
	public IJavaFieldType getType();

	public IJavaTypeReflection<F> getFieldType();
}
