package com.g2forge.alexandria.java.platform;

public interface IPlatformNamed {
	public static String getPlatformName(String prefix, String base, String suffix) {
		final StringBuilder retVal = new StringBuilder();
		if ((prefix != null) && !base.startsWith(prefix)) retVal.append(prefix);
		retVal.append(base);
		if ((suffix != null) && !retVal.toString().endsWith(suffix)) retVal.append(suffix);
		return retVal.toString();
	}

	public String getPlatformName(String base);
}
