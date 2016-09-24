package com.g2forge.alexandria.java.core.helpers;

import java.util.Scanner;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class ResourceHelpers {
	public static String read(Class<?> klass, String resource) {
		try (final Scanner scanner = new Scanner(klass.getResourceAsStream(resource), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
