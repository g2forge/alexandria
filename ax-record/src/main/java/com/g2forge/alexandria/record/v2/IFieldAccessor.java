package com.g2forge.alexandria.record.v2;

import com.g2forge.alexandria.java.name.IStringNamed;
import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IFieldAccessor<Record, Field> extends IGenericTyped<Field, IFieldType<? super Record, ? super Record, Field>>, ITuple1GS<Field>, IStringNamed {
	@Override
	public default String getName() {
		return getType().getName();
	}

	public IRecordAccessor<Record> getRecordAccessor();
}
