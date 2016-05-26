package com.g2forge.alexandria.java.reflection.type;

import lombok.Data;

@Data
public class TypeReference<T> implements ITypeReference<T> {
	protected final Class<T> type;

	@Override
	public Class<? super T> getErased() {
		return getType();
	}
}
