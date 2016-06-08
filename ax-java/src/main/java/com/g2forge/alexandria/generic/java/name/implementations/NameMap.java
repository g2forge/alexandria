package com.g2forge.alexandria.generic.java.name.implementations;

import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.generic.java.name.INamed;

public class NameMap<T extends INamed<? extends N>, N> implements IMap1<T, N> {
	@Override
	public N map(final T input) {
		return input.getName();
	}
}
