package com.g2forge.alexandria.generic.record.map.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.generic.record.IFieldType;
import com.g2forge.alexandria.generic.record.IRecordType;
import com.g2forge.alexandria.java.CollectionHelpers;

public class MapRecordType implements IRecordType<MapRecord> {
	protected final Collection<MapFieldType<?>> fieldTypes;

	/**
	 * @param fieldTypes
	 */
	public MapRecordType(final Collection<? extends IFieldType<MapRecord, ?>> fieldTypes) {
		this.fieldTypes = new ArrayList<>();
		if (fieldTypes != null) for (IFieldType<MapRecord, ?> fieldType : fieldTypes) {
			if (fieldType instanceof MapFieldType) this.fieldTypes.add((MapFieldType<?>) fieldType);
			else this.fieldTypes.add(new MapFieldType<>(fieldType));
		}
	}

	/**
	 * @param fieldTypes
	 */
	@SafeVarargs
	public MapRecordType(final IFieldType<MapRecord, ?>... fieldTypes) {
		this(CollectionHelpers.asList(fieldTypes));
	}

	@Override
	public MapRecord create() {
		return new MapRecord();
	}

	@Override
	public Collection<? extends IFieldType<MapRecord, ?>> getFields() {
		return Collections.unmodifiableCollection(fieldTypes);
	}
}
