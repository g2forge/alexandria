package com.g2forge.alexandria.java.typed;

public interface IDynamicType<T> {
	public T cast(Object value);

	public boolean isInstance(Object value);
}
