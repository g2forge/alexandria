package com.g2forge.alexandria.generic.java.filter.implementations;

import com.g2forge.alexandria.generic.java.ObjectHelpers;
import com.g2forge.alexandria.generic.java.filter.AFilter;

public class MemberFilter extends AFilter<Object> {
	protected final Object[] matches;
	
	@SafeVarargs
	public <T> MemberFilter(final T... matches) {
		this.matches = matches;
	}
	
	@Override
	public boolean isAccepted(final Object value) {
		for (final Object match : matches) {
			if (ObjectHelpers.equals(match, value)) return true;
		}
		return false;
	}
}
