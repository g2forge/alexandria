package com.g2forge.alexandria.record.v2.accessor2;

import com.g2forge.alexandria.java.tuple.ITuple1_S;
import com.g2forge.alexandria.record.v2.type.IFieldType;

public interface IFieldAccessor_S<Record, Field> extends IFieldAccessor__<Record, Field>, ITuple1_S<Field> {
	@SuppressWarnings("unchecked" /* The java compiler cannot type check this, but since getType is bound to return IFieldType<,,Field> this must statically be correct. */)
	public default <F> IFieldAccessor_S<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessor_S<Record, F>) this;
	}

	@Override
	public IFieldType<?, ? super Record, Field> getType();
}
