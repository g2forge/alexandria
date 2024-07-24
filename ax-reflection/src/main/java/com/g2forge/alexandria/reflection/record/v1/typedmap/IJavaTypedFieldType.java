package com.g2forge.alexandria.reflection.record.v1.typedmap;

import com.g2forge.alexandria.adt.record.v1.IFieldType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;

public interface IJavaTypedFieldType<R, F> extends IFieldType<R, F>, IJavaGenericTyped<F> {}
