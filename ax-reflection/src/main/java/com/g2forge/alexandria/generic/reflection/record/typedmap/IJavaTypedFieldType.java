package com.g2forge.alexandria.generic.reflection.record.typedmap;

import com.g2forge.alexandria.generic.record.IFieldType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;

public interface IJavaTypedFieldType<R, F> extends IFieldType<R, F>, IJavaGenericTyped<F> {}
