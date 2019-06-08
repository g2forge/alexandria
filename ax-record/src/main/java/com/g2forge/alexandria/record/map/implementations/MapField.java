package com.g2forge.alexandria.record.map.implementations;

import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.record.IField;
import com.g2forge.alexandria.record.IFieldType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapField<T> implements IField<MapRecord, T> {
	protected final MapFieldType<T> type;

	protected final MapRecord record;

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
	public IFieldType<MapRecord, T> getType() {
		return type;
	}
}
