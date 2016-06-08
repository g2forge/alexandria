package com.g2forge.alexandria.generic.record;

import com.g2forge.alexandria.generic.java.tuple.ITuple1GS;
import com.g2forge.alexandria.generic.java.typed.IGenericTyped;

public interface IField<R, F> extends IGenericTyped<F, IFieldType<R, F>> {
	public ITuple1GS<F> getAccessor();
}
