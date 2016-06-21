package com.g2forge.alexandria.record;

import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IField<R, F> extends IGenericTyped<F, IFieldType<R, F>> {
	public ITuple1GS<F> getAccessor();
}
