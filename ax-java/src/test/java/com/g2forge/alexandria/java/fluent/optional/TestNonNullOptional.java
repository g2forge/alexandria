package com.g2forge.alexandria.java.fluent.optional;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public class TestNonNullOptional extends ATestOptional {
	@Override
	protected IOptionalFactory getFactory() {
		return NonNullOptional.FACTORY;
	}

	@Test(expected = NullPointerException.class)
	public void ofNull() {
		Assert.assertTrue(getFactory().of(null).isEmpty());
	}
}
