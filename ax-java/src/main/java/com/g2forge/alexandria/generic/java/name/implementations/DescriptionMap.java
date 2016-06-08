package com.g2forge.alexandria.generic.java.name.implementations;

import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.generic.java.name.IDescribed;

public class DescriptionMap<T extends IDescribed<? extends D>, D> implements IMap1<T, D> {
	@Override
	public D map(final T input) {
		return input.getDescription();
	}
}
