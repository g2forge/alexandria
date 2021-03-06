package com.g2forge.alexandria.adt.record.v2.accessor2;

import com.g2forge.alexandria.adt.record.v2.type.IFieldType;
import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;

public interface IFieldAccessorG_<Record, Field> extends IFieldAccessor__<Record, Field>, ITuple1G_<Field> {
	@SuppressWarnings("unchecked" /* The java compiler cannot type check this, but since getType is bound to return IFieldType<,,Field> this must statically be correct. */)
	public default <F> IFieldAccessorG_<Record, F> as(IFieldType<?, ?, F> fieldType) {
		if (!getType().equals(fieldType)) return null;
		return (IFieldAccessorG_<Record, F>) this;
	}

	@Override
	public IFieldType<? super Record, ?, Field> getType();
}
