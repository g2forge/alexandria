package com.g2forge.alexandria.java.reference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StrongReference<T> implements IReference<T> {
	protected T referent;

	@Override
	public void clear() {
		referent = null;
	}

	@Override
	public T get() {
		return referent;
	}
}
