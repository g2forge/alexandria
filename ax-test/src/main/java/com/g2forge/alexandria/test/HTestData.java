package com.g2forge.alexandria.test;

import org.apache.commons.lang3.RandomStringUtils;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HTestData {
	public static String randomEmail(final int length) {
		return RandomStringUtils.randomAlphanumeric(length) + "@example.com";
	}
}
