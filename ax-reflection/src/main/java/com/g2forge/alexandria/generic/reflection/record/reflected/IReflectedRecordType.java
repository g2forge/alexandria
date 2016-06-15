package com.g2forge.alexandria.generic.reflection.record.reflected;

import java.util.Collection;

import com.g2forge.alexandria.generic.reflection.record.typedmap.IJavaTypedRecordType;

public interface IReflectedRecordType<R> extends IJavaTypedRecordType<R> {
	@Override
	public Collection<? extends IReflectedFieldType<R, ?>> getFields();
}
