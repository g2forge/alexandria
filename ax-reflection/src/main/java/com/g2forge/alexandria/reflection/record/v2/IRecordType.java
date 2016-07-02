package com.g2forge.alexandria.reflection.record.v2;

import java.util.Collection;

public interface IRecordType {
	public Collection<? extends IPropertyType> getProperties();
}
