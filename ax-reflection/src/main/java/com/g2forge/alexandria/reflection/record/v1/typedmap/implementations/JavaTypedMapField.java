package com.g2forge.alexandria.reflection.record.v1.typedmap.implementations;

import com.g2forge.alexandria.adt.record.v1.IField;
import com.g2forge.alexandria.adt.record.v1.IFieldType;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JavaTypedMapField<T> implements IField<JavaTypedMapRecord, T> {
	protected final JavaTypedMapFieldType<T> type;

	protected final JavaTypedMapRecord record;

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
