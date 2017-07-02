package com.g2forge.alexandria.service;

import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestSplitTypedServiceLoader {
	public static interface IKey {}

	public static interface IType {
		public default String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class Service1 implements IType {}

	public static class Service2 implements IType {}

	@Test
	public void test() {
		final IServiceLoader<IType> loader = new SplitTypedServiceLoader<>(IKey.class, IType.class);
		Assert.assertEquals(Service1.class.getSimpleName(), loader.load(Service1.class).findAny().get().getValue());
		Assert.assertEquals(Service2.class.getSimpleName(), loader.load(Service2.class).findAny().get().getValue());
		Assert.assertEquals(2l, loader.load().collect(Collectors.counting()).longValue());
	}

	public static void main(String... args) {
		HService.writeSPIFile(IKey.class, IType.class, HCollection.asList(Service1.class, Service2.class));
	}
}
