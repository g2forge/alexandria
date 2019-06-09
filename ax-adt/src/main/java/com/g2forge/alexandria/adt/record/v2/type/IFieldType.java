package com.g2forge.alexandria.adt.record.v2.type;

import com.g2forge.alexandria.java.adt.name.IStringNamed;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;

/**
 * @param <Get> The record type on which get operations can be used.
 * @param <Set> The record type on which set operations can be used. May be different than <code>Get</code> in order to support immutable record types with
 *            builders.
 * @param <Field> The type of the field itself.
 */
public interface IFieldType<Get, Set, Field> extends IStringNamed {
	/**
	 * Getter to access the value of this field.
	 */
	public IFunction1<? super Get, ? extends Field> getGetter();

	/**
	 * Set the value of this field.
	 */
	public IConsumer2<? super Set, ? super Field> getSetter();
}
