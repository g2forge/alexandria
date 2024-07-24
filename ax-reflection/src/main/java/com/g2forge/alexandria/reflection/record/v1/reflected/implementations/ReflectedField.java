package com.g2forge.alexandria.reflection.record.v1.reflected.implementations;

import com.g2forge.alexandria.adt.record.v1.IField;
import com.g2forge.alexandria.adt.record.v1.IFieldType;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;

import lombok.Getter;

public class ReflectedField<R, T> implements IField<R, T> {
	protected final ReflectedFieldType<R, T> type;

	@Getter
	protected final ITuple1GS<T> accessor;

	public ReflectedField(final ReflectedFieldType<R, T> type, final R record) {
		this.type = type;
		this.accessor = type.field.getAccessor(record);
	}

	@Override
	public IFieldType<R, T> getType() {
		return type;
	}
}
