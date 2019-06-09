package com.g2forge.alexandria.adt.record.v2.accessor2;

import com.g2forge.alexandria.adt.record.v2.type.IRecordType;
import com.g2forge.alexandria.java.type.IGenericTyped;

public interface IRecordAccessor__<Record> extends IGenericTyped<Record, IRecordType<?, ?>> {
	public Record getRecord();
}
