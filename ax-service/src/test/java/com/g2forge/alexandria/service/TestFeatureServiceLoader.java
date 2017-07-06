package com.g2forge.alexandria.service;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

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
		HService.writeSPIFile(IService.class, Service1.class, Service2.class);
	}

	@Test
	public void test() {
		final IServiceLoader<IService> loader = new FeatureServiceLoader<>(IService.class);
		// Test the specific loads
		Assert.assertEquals(Service1.class.getSimpleName(), loader.load(Service1.class).stream().findAny().get().getValue());
		Assert.assertEquals(Service2.class.getName(), loader.load(Service2.class).stream().findAny().get().getValue());

		// Test feature based shadowing
		final Collection<? extends IService> services = loader.load().toCollection();
		Assert.assertEquals(1, services.size());
		Assert.assertTrue(HCollection.getOne(services) instanceof IFancy);
	}
}
