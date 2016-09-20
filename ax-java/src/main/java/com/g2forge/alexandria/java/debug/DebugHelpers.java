package com.g2forge.alexandria.java.debug;

public class DebugHelpers {
	protected static boolean isDebugEnabled;

	static {
		final String string = System.getProperty(DebugHelpers.class.getName() + ".isDebugEnabled");
		if (string == null) isDebugEnabled = false;
		else isDebugEnabled = Boolean.valueOf(string);
	}

	public static boolean isDebugEnabled() {
		return isDebugEnabled;
	}
}
