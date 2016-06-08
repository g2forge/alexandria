package com.g2forge.alexandria.generic.record;

import com.g2forge.alexandria.generic.java.typed.IGenericTyped;
import com.g2forge.alexandria.java.tuple.ITuple1GS;

public interface IField<R, F> extends IGenericTyped<F, IFieldType<R, F>> {
	public ITuple1GS<F> getAccessor();
}
