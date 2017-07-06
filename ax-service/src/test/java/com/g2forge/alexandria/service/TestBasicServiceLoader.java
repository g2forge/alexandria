package com.g2forge.alexandria.service;

import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HStream;

public class TestBasicServiceLoader {
	public static interface IServiceA {
		public String getValue();
	}

	public static interface IServiceBKey {}

	public static interface IServiceBType {
		public default String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class ServiceA1 implements IServiceA {
		@Override
		public String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class ServiceA2 implements IServiceA {
		@Override
		public String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class ServiceB1 implements IServiceBType {}

	public static class ServiceB2 implements IServiceBType {}

	public static void main(String... args) {
		HService.writeSPIFile(IServiceA.class, ServiceA1.class, ServiceA2.class);
		HService.writeSPIFile(IServiceBKey.class, IServiceBType.class, HCollection.asList(ServiceB1.class, ServiceB2.class));
	}

	@Test
	public void a() {
		final IServiceLoader<IServiceA> loader = new BasicServiceLoader<>(IServiceA.class);
		Assert.assertEquals(ServiceA1.class.getSimpleName(), HStream.subtype(loader.load().stream(), ServiceA1.class).findAny().get().getValue());
		Assert.assertEquals(ServiceA2.class.getSimpleName(), HStream.subtype(loader.load().stream(), ServiceA2.class).findAny().get().getValue());
		Assert.assertEquals(2l, loader.load().stream().collect(Collectors.counting()).longValue());
	}

	@Test
	public void b() {
		final IServiceLoader<IServiceBType> loader = new BasicServiceLoader<>(IServiceBKey.class, IServiceBType.class);
		Assert.assertEquals(ServiceB1.class.getSimpleName(), HStream.subtype(loader.load().stream(), ServiceB1.class).findAny().get().getValue());
		Assert.assertEquals(ServiceB2.class.getSimpleName(), HStream.subtype(loader.load().stream(), ServiceB2.class).findAny().get().getValue());
		Assert.assertEquals(2l, loader.load().stream().collect(Collectors.counting()).longValue());
	}
}
