package com.g2forge.alexandria.service;

import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class TestBasicServiceLoader {
	public static interface IService {
		public String getValue();
	}

	public static class Service1 implements IService {
		@Override
		public String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class Service2 implements IService {
		@Override
		public String getValue() {
			return getClass().getSimpleName();
		}
	}

	@Test
	public void test() {
		final IServiceLoader<IService> loader = new BasicServiceLoader<>(IService.class);
		Assert.assertEquals(Service1.class.getSimpleName(), loader.load(Service1.class).findAny().get().getValue());
		Assert.assertEquals(Service2.class.getSimpleName(), loader.load(Service2.class).findAny().get().getValue());
		Assert.assertEquals(2l, loader.load().collect(Collectors.counting()).longValue());
	}

	public static void main(String... args) {
		HService.writeSPIFile(TestBasicServiceLoader.IService.class, TestBasicServiceLoader.Service1.class);
		HService.writeSPIFile(TestBasicServiceLoader.IService.class, TestBasicServiceLoader.Service2.class);
	}
}
