package com.g2forge.alexandria.command.invocation.environment;

import java.util.Map;
import java.util.Objects;

import org.junit.Test;

import com.g2forge.alexandria.adt.associative.map.MapBuilder;
import com.g2forge.alexandria.test.HAssert;

public class TestSystemEnvironment {
	@Test
	public void caseInsensitive() {
		final String key = "Key", value = "Value";
		final Map<String, String> inputMap = new MapBuilder<>(key, value).build();
		final Map<String, String> outputMap = new SystemEnvironment() {
			@Override
			protected Map<String, String> getSystemEnvironment() {
				return inputMap;
			}

			@Override
			protected boolean isCaseInsensitive() {
				return true;
			}
		}.toMap();
		HAssert.assertEquals(value, outputMap.get(key));
		HAssert.assertEquals(value, outputMap.get(key.toUpperCase()));
		HAssert.assertEquals(value, outputMap.get(key.toLowerCase()));
	}

	@Test
	public void caseSensitive() {
		final String key = "Key", value = "Value";
		final Map<String, String> inputMap = new MapBuilder<>(key, value).build();
		final Map<String, String> outputMap = new SystemEnvironment() {
			@Override
			protected Map<String, String> getSystemEnvironment() {
				return inputMap;
			}

			@Override
			protected boolean isCaseInsensitive() {
				return false;
			}
		}.toMap();
		HAssert.assertEquals(value, outputMap.get(key));
		HAssert.assertFalse(Objects.equals(value, outputMap.get(key.toUpperCase())) && Objects.equals(value, outputMap.get(key.toLowerCase())));
	}
}
