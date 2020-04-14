package com.g2forge.alexandria.java.platform;

public interface IPlatformNamed {
	public default String getPlatformName(String name) {
		final String prefix = getPrefix();
		final String suffix = getSuffix();
		if ((prefix == null) && (suffix == null)) return name;

		final StringBuilder retVal = new StringBuilder();
		if ((prefix != null) && !name.startsWith(prefix)) retVal.append(prefix);
		retVal.append(name);
		if ((suffix != null) && !retVal.toString().endsWith(suffix)) retVal.append(suffix);
		return retVal.toString();
	}

	public String getPrefix();

	public String getSuffix();
}
