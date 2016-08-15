package com.g2forge.alexandria.java.core.helpers;

import java.util.Scanner;

public class ResourceHelpers {
	public static String read(Class<?> klass, String resource) {
		try (final Scanner scanner = new Scanner(klass.getResourceAsStream(resource), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
