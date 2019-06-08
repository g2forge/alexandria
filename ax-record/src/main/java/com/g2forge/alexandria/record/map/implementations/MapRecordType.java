package com.g2forge.alexandria.record.map.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.record.IFieldType;
import com.g2forge.alexandria.record.IRecordType;

public class MapRecordType implements IRecordType<MapRecord> {
	protected final Collection<MapFieldType<?>> fieldTypes;

	public MapRecordType(final Collection<? extends IFieldType<MapRecord, ?>> fieldTypes) {
		this.fieldTypes = new ArrayList<>();
		if (fieldTypes != null) for (IFieldType<MapRecord, ?> fieldType : fieldTypes) {
			if (fieldType instanceof MapFieldType) this.fieldTypes.add((MapFieldType<?>) fieldType);
			else this.fieldTypes.add(new MapFieldType<>(fieldType));
		}
	}

	@SafeVarargs
	public MapRecordType(final IFieldType<MapRecord, ?>... fieldTypes) {
		this(HCollection.asList(fieldTypes));
	}

	@Override
	public MapRecord get() {
		return new MapRecord();
	}

	@Override
	public Collection<? extends IFieldType<MapRecord, ?>> getFields() {
		return Collections.unmodifiableCollection(fieldTypes);
	}
}
