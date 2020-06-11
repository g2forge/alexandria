package com.g2forge.alexandria.service;

import org.junit.Test;

import com.g2forge.alexandria.java.function.type.ITypeFunction1;
import com.g2forge.alexandria.test.HAssert;

public class TestDefaultInstantiator extends ATestInstantiator {
	@Override
	protected ITypeFunction1<IInterface> getInstantiator() {
		return new DefaultInstantiator<>(IInterface.class);
	}

	@Test
	public void normal() {
		HAssert.assertInstanceOf(NormalClass.class, getInstantiator().apply(NormalClass.class));
	}
	
	@Test
	public void singleton() {
		HAssert.assertInstanceOf(SingletonClass.class, getInstantiator().apply(SingletonClass.class));
	}
}
