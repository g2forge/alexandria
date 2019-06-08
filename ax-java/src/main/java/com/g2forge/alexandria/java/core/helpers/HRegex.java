package com.g2forge.alexandria.java.core.helpers;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HRegex {
	public static final String UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	public static String toList(String element, String separator) {
		return element + "(?:" + separator + element + ")*";
	}
}
