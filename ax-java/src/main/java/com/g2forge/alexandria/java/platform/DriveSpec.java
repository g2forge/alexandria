package com.g2forge.alexandria.java.platform;

import java.util.regex.Pattern;

public enum DriveSpec {
	WINDOWS {
		@Override
		public String canonize(String separator, String path) {
			final String qseparator = Pattern.quote(separator);
			if ("\\".equals(separator)) separator = "\\\\";
			path = path.replaceFirst("^" + qseparator + "?([A-Za-z]):?" + qseparator, "$1:" + separator);
			return path.replaceFirst("^" + qseparator + "cygdrive" + qseparator + "([A-Za-z]):?" + qseparator, "$1:" + separator);
		}
	},
	CYGWIN {
		@Override
		public String canonize(String separator, String path) {
			final String qseparator = Pattern.quote(separator);
			if ("\\".equals(separator)) separator = "\\\\";
			return path.replaceFirst("^" + qseparator + "?([A-Za-z]):?" + qseparator, separator + "cygdrive" + separator + "$1" + separator);
		}
	},
	ROOTED {
		@Override
		public String canonize(String separator, String path) {
			final String qseparator = Pattern.quote(separator);
			if ("\\".equals(separator)) separator = "\\\\";
			path = path.replaceFirst("^" + qseparator + "?([A-Za-z]):?" + qseparator, separator + "$1:" + separator);
			return path.replaceFirst("^" + qseparator + "cygdrive" + qseparator + "([A-Za-z]):?" + qseparator, separator + "$1:" + separator);
		}
	};

	public abstract String canonize(String separator, String path);
}
