package com.g2forge.alexandria.record.v2;

import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IFieldAccessor<Record, Field> extends IGenericTyped<Field, IFieldType<? super Record, ? super Record, Field>>, ITuple1GS<Field> {
	@SuppressWarnings("unchecked")
	public default <F> IFieldAccessor<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessor<Record, F>) this;
	}

	public IRecordAccessor<Record> getRecordAccessor();
}
