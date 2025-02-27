package com.g2forge.alexandria.java.platform;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.text.HString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
		// Canonize the drive spec
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

	public String toString(Path path) {
		final String fileSeparator = getFileSeparator();
		if (path == null) return fileSeparator;
		final StringBuilder retVal = new StringBuilder();
		if (path.getRoot() != null) {
			if (HPlatform.getPlatform().getCategory() == PlatformCategory.Microsoft) retVal.append(HString.stripSuffix(path.getRoot().toString(), fileSeparator));
			else retVal.append(path.getRoot().toString());
		}
		for (int i = 0; i < path.getNameCount(); i++) {
			if (!retVal.isEmpty()) retVal.append(fileSeparator);
			retVal.append(path.getName(i).toString());
		}
		return retVal.toString();
	}
}
