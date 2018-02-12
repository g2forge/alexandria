package com.g2forge.alexandria.java.platform;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PathSpec {
	WinBack(";", "\\", DriveSpec.WINDOWS, false),
	WinForward(";", "/", DriveSpec.WINDOWS, false),
	CygWin(":", "/", DriveSpec.CYGWIN, true),
	UNIX(":", "/", DriveSpec.ROOTED, true);

	protected final String pathSeparator;

	protected final String fileSeparator;

	protected final DriveSpec driveSpec;

	protected final boolean caseSensitive;

	public String canonizePath(String path) {
		// Convert everyone else's file separators to ours
		for (PathSpec pathSpec : PathSpec.values()) {
			if (pathSpec.getFileSeparator().equals(getFileSeparator())) continue;
			path = path.replace(pathSpec.getFileSeparator(), getFileSeparator());
		}
		// Convert the drive spec
		return getDriveSpec().canonize(getFileSeparator(), path);
	}

	public String canonizePaths(String path) {
		final StringBuilder retVal = new StringBuilder();
		for (String part : splitPaths(path)) {
			if (retVal.length() > 0) retVal.append(getPathSeparator());
			retVal.append(canonizePath(part));
		}
		return retVal.toString();
	}

	public String joinPaths(String... paths) {
		return Stream.of(paths).map(this::canonizePath).collect(Collectors.joining(getPathSeparator()));
	}

	public String[] splitPaths(String path) {
		if (path == null) return new String[0];
		return path.split(Pattern.quote(getPathSeparator()) + "+");
	}
}
