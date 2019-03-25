package com.g2forge.alexandria.java.type;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;

public interface IDynamicType<T> {
	public T cast(Object value);

	@TODO("All implementations of this method need to be implemented in a generic-safe manner")
	public default boolean isAssignableFrom(IDynamicType<?> type) {
		throw new NotYetImplementedError();
	}

	public boolean isInstance(Object value);
}
