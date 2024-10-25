package com.g2forge.alexandria.java.platform;

import java.nio.file.Path;

public enum PlatformCategory {
	Posix {
		@Override
		public String convertExecutablePathToString(Path executablePath) {
			if (executablePath.isAbsolute()) return executablePath.toString();
			return "./" + executablePath.toString();
		}
	},
	Microsoft;

	public String convertExecutablePathToString(Path executablePath) {
		return executablePath.toString();
	}
}
