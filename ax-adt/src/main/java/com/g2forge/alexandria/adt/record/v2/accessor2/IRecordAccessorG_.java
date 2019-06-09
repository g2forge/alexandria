package com.g2forge.alexandria.adt.record.v2.accessor2;

import com.g2forge.alexandria.adt.record.v2.type.IRecordType;

public interface IRecordAccessorG_<Record> extends IRecordAccessor__<Record> {
	@Override
	public IRecordType<Record, ?> getType();
}
