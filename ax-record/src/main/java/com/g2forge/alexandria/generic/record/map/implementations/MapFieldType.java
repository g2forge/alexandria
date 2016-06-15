package com.g2forge.alexandria.generic.record.map.implementations;

import java.util.Objects;

import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.record.IFieldType;

public class MapFieldType<T> implements IFieldType<MapRecord, T> {
	protected final String name;

	/**
	 * @param fieldType
	 */
	public MapFieldType(IFieldType<MapRecord, T> fieldType) {
		this(fieldType.getName());
	}

	/**
	 * @param name
	 */
	public MapFieldType(String name) {
		this.name = name;
	}

	@Override
	public IField<MapRecord, T> apply(final MapRecord input) {
		return new MapField<T>(this, input);
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
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

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}
}
