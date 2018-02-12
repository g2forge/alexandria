package com.g2forge.alexandria.java.platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class APlatformNamed implements IPlatformNamed {
	/**
	 * TODO: Javadoc
	 * 
	 * @param _prefix
	 * @param base
	 * @param _suffix
	 * @return
	 */
	public static String getPlatformName(String prefix, String base, String suffix) {
		final StringBuilder retVal = new StringBuilder();
		if ((prefix != null) && !base.startsWith(prefix)) retVal.append(prefix);
		retVal.append(base);
		if ((suffix != null) && !retVal.toString().endsWith(suffix)) retVal.append(suffix);
		return retVal.toString();
	}

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String base) {
		return getPlatformName(prefix, base, suffix);
	}
}
