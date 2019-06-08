package com.g2forge.alexandria.record;

import java.util.Collection;

import com.g2forge.alexandria.java.function.ISupplier;
import com.g2forge.alexandria.java.type.IGeneric;

public interface IRecordType<R> extends IGeneric<R>, ISupplier<R> {
	public Collection<? extends IFieldType<R, ?>> getFields();
}
