package com.g2forge.alexandria.record.v2.accessor;

import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.java.typed.IGenericTyped;
import com.g2forge.alexandria.record.v2.type.IFieldType;

public interface IFieldAccessor<Record, Field> extends IGenericTyped<Field, IFieldType<? super Record, ? super Record, Field>>, ITuple1GS<Field> {
	@SuppressWarnings("unchecked" /* The java compiler cannot type check this, but since getType is bound to return IFieldType<,,Field> this must statically be correct. */)
	public default <F> IFieldAccessor<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessor<Record, F>) this;
	}

	public IRecordAccessor<Record> getRecordAccessor();
}
