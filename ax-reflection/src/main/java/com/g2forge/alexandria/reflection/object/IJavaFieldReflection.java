package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.java.tuple.ITuple1GS;

public interface IJavaFieldReflection<O, F> extends IJavaMemberReflection<O, F> {
	public ITuple1GS<F> getAccessor(O object);
	
	@Override
	public IJavaFieldType getType();
}
