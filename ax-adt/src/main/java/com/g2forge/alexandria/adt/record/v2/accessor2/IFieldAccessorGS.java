package com.g2forge.alexandria.adt.record.v2.accessor2;

import com.g2forge.alexandria.adt.record.v2.type.IFieldType;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;

public interface IFieldAccessorGS<Record, Field> extends IFieldAccessorG_<Record, Field>, IFieldAccessor_S<Record, Field>, ITuple1GS<Field> {
	@SuppressWarnings("unchecked" /* The java compiler cannot type check this, but since getType is bound to return IFieldType<,,Field> this must statically be correct. */)
	public default <F> IFieldAccessorGS<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessorGS<Record, F>) this;
	}

	@Override
	public IFieldType<? super Record, ? super Record, Field> getType();
}
