package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;
import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;

public interface IJavaFieldReflection<O, F> extends IJavaGenericTyped<F>, IJavaAnnotated {
	public ITuple1GS<F> getAccessor(O object);
	
	@Override
	public IJavaFieldType getType();
}
