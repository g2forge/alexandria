package com.g2forge.alexandria.record;

import com.g2forge.alexandria.java.typed.ITyped;

public interface IRecord<RT extends IRecordType<? extends IRecord<RT, FT>>, FT extends IFieldType<?, ?>> extends ITyped<RT> {}
