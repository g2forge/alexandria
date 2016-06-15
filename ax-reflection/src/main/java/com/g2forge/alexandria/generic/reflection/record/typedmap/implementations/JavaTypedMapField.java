package com.g2forge.alexandria.generic.reflection.record.typedmap.implementations;

import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.record.IFieldType;
import com.g2forge.alexandria.java.tuple.ITuple1GS;

public class JavaTypedMapField<T> implements IField<JavaTypedMapRecord, T> {
	protected final JavaTypedMapFieldType<T> type;
	
	protected final JavaTypedMapRecord record;
	
	/**
	 * @param type
	 * @param record
	 */
	public JavaTypedMapField(final JavaTypedMapFieldType<T> type, final JavaTypedMapRecord record) {
		this.type = type;
		this.record = record;
	}
	
	@Override
	public ITuple1GS<T> getAccessor() {
		return new ITuple1GS<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T get0() {
				return (T) record.values.get(type.getName());
			}
			
			@Override
			public ITuple1GS<T> set0(final T value) {
				record.values.put(type.getName(), value);
				return this;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public T swap0(final T value) {
				return (T) record.values.put(type.getName(), value);
			}
		};
	}
	
	@Override
	public IFieldType<JavaTypedMapRecord, T> getType() {
		return type;
	}
}
