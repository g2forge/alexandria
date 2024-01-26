package com.g2forge.alexandria.command.invocation.environment.modified;

import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import com.g2forge.alexandria.adt.associative.map.MapBuilder;
import com.g2forge.alexandria.command.invocation.environment.MapEnvironment;
import com.g2forge.alexandria.command.invocation.environment.SystemEnvironment;
import com.g2forge.alexandria.test.HAssert;

public class TestModifiedEnvironment {
	@Test
	public void unspecified() {
		final String key = "Key", value = "Value";
		final ModifiedEnvironment environment = ModifiedEnvironment.builder().base(new MapEnvironment(new MapBuilder<>(key, value).build())).modifier(key, EnvironmentModifier.Unspecified).build();
		HAssert.assertNull(environment.apply(key));
		HAssert.assertTrue(environment.toMap().isEmpty());
	}

	@Test
	public void inherit() {
		final String key = "Key", value = "Value";
		final ModifiedEnvironment environment = ModifiedEnvironment.builder().base(new MapEnvironment(new MapBuilder<>(key, value).build())).modifier(key, EnvironmentModifier.Inherit).build();
		HAssert.assertEquals(value, environment.apply(key));
		HAssert.assertEquals(1, environment.toMap().size());
	}

	@Test
	public void value() {
		final String key = "Key", value = "Value", other = "Other";
		final ModifiedEnvironment environment = ModifiedEnvironment.builder().base(new MapEnvironment(new MapBuilder<>(key, value).build())).modifier(key, new EnvironmentValue(other)).build();
		HAssert.assertEquals(other, environment.apply(key));
		HAssert.assertEquals(1, environment.toMap().size());
	}

	@Test
	public void caseInsensitive() {
		final String key = "Key", value = "Value";
		final Map<String, String> inputMap = new MapBuilder<>(key, value).build();
		final Map<String, String> outputMap = ModifiedEnvironment.builder().base(new SystemEnvironment() {
			@Override
			protected Map<String, String> getSystemEnvironment() {
				return inputMap;
			}

			@Override
			protected boolean isCaseInsensitive() {
				return true;
			}
		}).build().toMap();
		HAssert.assertEquals(value, outputMap.get(key));
		HAssert.assertEquals(value, outputMap.get(key.toUpperCase()));
		HAssert.assertEquals(value, outputMap.get(key.toLowerCase()));
		HAssert.assertInstanceOf(SortedMap.class, outputMap);
	}
}
