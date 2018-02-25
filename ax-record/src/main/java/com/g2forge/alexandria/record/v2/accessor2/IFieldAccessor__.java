package com.g2forge.alexandria.record.v2.accessor2;

import com.g2forge.alexandria.java.tuple.ITuple1__;
import com.g2forge.alexandria.java.typed.IGenericTyped;
import com.g2forge.alexandria.record.v2.accessor.IRecordAccessor;
import com.g2forge.alexandria.record.v2.type.IFieldType;

public interface IFieldAccessor__<Record, Field> extends IGenericTyped<Field, IFieldType<?, ?, Field>>, ITuple1__<Field> {
	@SuppressWarnings("unchecked" /* The java compiler cannot type check this, but since getType is bound to return IFieldType<,,Field> this must statically be correct. */)
	public default <F> IFieldAccessor__<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessor__<Record, F>) this;
	}

	public IRecordAccessor<Record> getRecordAccessor();
}
