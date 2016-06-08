package com.g2forge.alexandria.generic.record;

import java.util.Collection;

import com.g2forge.alexandria.generic.java.typed.IGeneric;
import com.g2forge.alexandria.java.IFactory;

public interface IRecordType<R> extends IGeneric<R>, IFactory<R> {
	public Collection<? extends IFieldType<R, ?>> getFields();
}
