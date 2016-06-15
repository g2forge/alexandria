package com.g2forge.alexandria.generic.test;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomEmailHelpers {
	public static String randomEmail(final int length) {
		return RandomStringUtils.randomAlphanumeric(length) + "@example.com";
	}
}
