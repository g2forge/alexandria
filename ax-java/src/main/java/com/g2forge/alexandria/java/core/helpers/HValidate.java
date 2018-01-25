package com.g2forge.alexandria.java.core.helpers;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HValidate {
	public static short inRange(short value, short lower, short upper) {
		if (value < lower) throw new IllegalArgumentException(String.format("%1$d < %2$d", value, lower));
		if (value >= upper) throw new IllegalArgumentException(String.format("%1$d >= %2$d", value, upper));
		return value;
	}

	public static int inRange(int value, int lower, int upper) {
		if (value < lower) throw new IllegalArgumentException(String.format("%1$d < %2$d", value, lower));
		if (value >= upper) throw new IllegalArgumentException(String.format("%1$d >= %2$d", value, upper));
		return value;
	}
}
