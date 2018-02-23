package com.g2forge.alexandria.record.v2;

import java.util.Map;

import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IRecordAccessor<Record> extends IGenericTyped<Record, IRecordType<Record, ?>> {
	public Map<? super String, ? extends IFieldAccessor<? super Record, ?>> getFields();

	public <Field> IFieldAccessor<? super Record, Field> getField(IFieldType<? super Record, ? super Record, Field> fieldType);

	public Record getRecord();
}
