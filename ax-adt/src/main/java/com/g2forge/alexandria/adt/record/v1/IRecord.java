package com.g2forge.alexandria.adt.record.v1;

import com.g2forge.alexandria.java.type.ITyped;

public interface IRecord<RT extends IRecordType<? extends IRecord<RT, FT>>, FT extends IFieldType<?, ?>> extends ITyped<RT> {}
