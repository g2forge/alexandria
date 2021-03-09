package com.g2forge.alexandria.java.type.ref;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class TestITypeRef {
	@Test
	public void equals() {
		Assert.assertEquals(ITypeRef.of(String.class), ITypeRef.of(String.class));
	}

	@Test
	public void capture() {
		final ITypeRef<Collection<String>> ref = new ATypeRef<Collection<String>>() {};
		final ParameterizedType parameterized = (ParameterizedType) ref.getType();
		Assert.assertEquals(Collection.class, parameterized.getRawType());
		Assert.assertEquals(String.class, parameterized.getActualTypeArguments()[0]);
	}

	@Test
	public void identity() {
		Assert.assertEquals(ITypeRef.of(String.class), new ATypeRef<String>() {});
	}
}
