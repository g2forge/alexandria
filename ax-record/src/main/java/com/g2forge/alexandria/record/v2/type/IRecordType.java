package com.g2forge.alexandria.record.v2.type;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.core.iface.IFactory;
import com.g2forge.alexandria.java.function.IFunction1;

/**
 * @param <Record> The java type of the record itself, which may be immutable.
 * @param <Builder> The java type to use when building a record, may be the same as <code>Record</code> when the type is mutable.
 */
public interface IRecordType<Record, Builder> {
	public IFunction1<? super Builder, ? extends Record> getBuilder();

	public IFactory<? extends Builder> getFactory();

	public default IFieldType<? super Record, ? super Builder, ?> getField(String name) {
		return HStream.findOne(getFields().stream().filter(field -> name.equals(field.getName())));
	}

	public Collection<? extends IFieldType<? super Record, ? super Builder, ?>> getFields();

	public default boolean isMutable() {
		return getBuilder() == IFunction1.identity();
	}
}
