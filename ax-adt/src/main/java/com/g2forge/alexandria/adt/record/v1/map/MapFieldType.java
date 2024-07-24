package com.g2forge.alexandria.adt.record.v1.map;

import java.util.Objects;

import com.g2forge.alexandria.adt.record.v1.IField;
import com.g2forge.alexandria.adt.record.v1.IFieldType;

public class MapFieldType<T> implements IFieldType<MapRecord, T> {
	protected final String name;

	public MapFieldType(IFieldType<MapRecord, T> fieldType) {
		this(fieldType.getName());
	}

	public MapFieldType(String name) {
		this.name = name;
	}

	@Override
	public IField<MapRecord, T> apply(final MapRecord input) {
		return new MapField<T>(this, input);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof MapFieldType)) return false;

		final MapFieldType<?> that = (MapFieldType<?>) obj;
		return Objects.equals(getName(), that.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}
}
