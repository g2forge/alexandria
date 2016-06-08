package com.g2forge.alexandria.generic.java.name.implementations;

import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.generic.java.name.IIdentified;

public class IdentifierMap<T extends IIdentified<? extends I>, I> implements IMap1<T, I> {
	@Override
	public I map(final T input) {
		return input.getIdentifier();
	}
}
