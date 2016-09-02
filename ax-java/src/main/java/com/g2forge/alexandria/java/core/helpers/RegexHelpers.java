package com.g2forge.alexandria.java.core.helpers;

public class RegexHelpers {
	public static final String UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	public static String toList(String element, String separator) {
		return element + "(?:" + separator + element + ")*";
	}
}
