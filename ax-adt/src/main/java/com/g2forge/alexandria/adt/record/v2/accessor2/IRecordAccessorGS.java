package com.g2forge.alexandria.adt.record.v2.accessor2;

import com.g2forge.alexandria.adt.record.v2.type.IRecordType;

public interface IRecordAccessorGS<Record> extends IRecordAccessorG_<Record>, IRecordAccessor_S<Record> {
	@Override
	public IRecordType<Record, Record> getType();
}
