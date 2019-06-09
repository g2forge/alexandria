package com.g2forge.alexandria.adt.record.v2.accessor;

import java.util.Collection;
import java.util.Objects;

import com.g2forge.alexandria.adt.record.v2.type.IFieldType;
import com.g2forge.alexandria.adt.record.v2.type.IRecordType;
import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.type.IGenericTyped;

public interface IRecordAccessor<Record> extends IGenericTyped<Record, IRecordType<Record, ?>> {
	public default <Field> IFieldAccessor<? super Record, Field> getField(IFieldType<? super Record, ? super Record, Field> fieldType) {
		return HStream.findOne(getFields().stream().map(field -> field.as(fieldType)).filter(Objects::nonNull));
	}

	public default IFieldAccessor<? super Record, ?> getField(String name) {
		return HStream.findOne(getFields().stream().filter(field -> name.equals(field.getType().getName())));
	}

	public Collection<? extends IFieldAccessor<? super Record, ?>> getFields();

	public Record getRecord();
}
