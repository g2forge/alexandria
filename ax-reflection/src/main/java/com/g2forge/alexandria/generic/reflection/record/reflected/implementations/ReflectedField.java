package com.g2forge.alexandria.generic.reflection.record.reflected.implementations;

import com.g2forge.alexandria.generic.java.tuple.ITuple1GS;
import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.record.IFieldType;

public class ReflectedField<R, T> implements IField<R, T> {
	protected final ReflectedFieldType<R, T> type;
	
	protected final ITuple1GS<T> accessor;
	
	/**
	 * @param type
	 * @param record
	 */
	public ReflectedField(final ReflectedFieldType<R, T> type, final R record) {
		this.type = type;
		this.accessor = type.field.getAccessor(record);
	}
	
	@Override
	public ITuple1GS<T> getAccessor() {
		return accessor;
	}
	
	@Override
	public IFieldType<R, T> getType() {
		return type;
	}
}
