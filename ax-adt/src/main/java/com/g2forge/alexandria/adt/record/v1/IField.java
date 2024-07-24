package com.g2forge.alexandria.adt.record.v1;

import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.java.type.IGenericTyped;

public interface IField<R, F> extends IGenericTyped<F, IFieldType<R, F>> {
	public ITuple1GS<F> getAccessor();
}
