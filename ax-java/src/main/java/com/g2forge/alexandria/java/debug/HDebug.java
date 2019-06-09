package com.g2forge.alexandria.java.debug;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HDebug {
	protected static boolean isDebugEnabled;

	static {
		final String string = System.getProperty(HDebug.class.getName() + ".isDebugEnabled");
		if (string == null) isDebugEnabled = false;
		else isDebugEnabled = Boolean.valueOf(string);
	}

	public static boolean isDebugEnabled() {
		return isDebugEnabled;
	}
}
