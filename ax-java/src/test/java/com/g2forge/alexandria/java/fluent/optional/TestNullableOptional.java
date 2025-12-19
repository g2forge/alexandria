package com.g2forge.alexandria.java.fluent.optional;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public class TestNullableOptional extends ATestOptional {
	@Override
	protected IOptionalFactory getFactory() {
		return NullableOptional.FACTORY;
	}

	@Test
	public void ofNull() {
		Assert.assertFalse(getFactory().of(null).isEmpty());
	}
	
	@Test
	@Override
	public void mapToNull() {
		Assert.assertFalse(getFactory().of(0).map(i -> null).isEmpty());
	}
}
