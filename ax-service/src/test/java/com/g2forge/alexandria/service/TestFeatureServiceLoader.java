package com.g2forge.alexandria.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class TestFeatureServiceLoader {
	public static interface IFancy extends IService {}

	public static interface IService {
		public String getValue();
	}

	public static class Service1 implements IService {
		@Override
		public String getValue() {
			return getClass().getSimpleName();
		}
	}

	public static class Service2 implements IFancy, IServiceFeatureHierarchy<IService> {
		@Override
		public Collection<Class<? extends IService>> getFeatureInterfaces() {
			return Arrays.asList(IFancy.class);
		}

		@Override
		public String getValue() {
			return getClass().getName();
		}
	}

	public static void main(String... args) {
		HService.writeSPIFile(TestFeatureServiceLoader.IService.class, TestFeatureServiceLoader.Service1.class);
		HService.writeSPIFile(TestFeatureServiceLoader.IService.class, TestFeatureServiceLoader.Service2.class);
	}

	@Test
	public void test() {
		final IServiceLoader<IService> loader = new FeatureServiceLoader<>(IService.class);
		// Test the specific loads
		Assert.assertEquals(Service1.class.getSimpleName(), loader.load(Service1.class).findAny().get().getValue());
		Assert.assertEquals(Service2.class.getName(), loader.load(Service2.class).findAny().get().getValue());
		
		// Test feature based shadowing
		final List<? extends IService> services = loader.load().collect(Collectors.toList());
		Assert.assertEquals(1, services.size());
		Assert.assertTrue(services.get(0) instanceof IFancy);
	}
}
