package com.g2forge.alexandria.record.v2.accessor2;

import com.g2forge.alexandria.java.typed.IGenericTyped;
import com.g2forge.alexandria.record.v2.type.IRecordType;

public interface IRecordAccessor__<Record> extends IGenericTyped<Record, IRecordType<?, ?>> {
	public Record getRecord();
}
