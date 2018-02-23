package com.g2forge.alexandria.record.v2;

import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.name.IStringNamed;

/**
 * 
 * @author greg
 *
 * @param <Get>
 * @param <Set>
 * @param <Field>
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
