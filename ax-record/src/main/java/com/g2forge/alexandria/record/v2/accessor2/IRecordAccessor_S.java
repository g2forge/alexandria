package com.g2forge.alexandria.record.v2.accessor2;

import com.g2forge.alexandria.record.v2.type.IRecordType;

public interface IRecordAccessor_S<Record> extends IRecordAccessor__<Record> {
	@Override
	public IRecordType<?, Record> getType();
}
