package com.g2forge.alexandria.java.platform;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.HString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PathSpec {
	WinBack(";", "\\", DriveSpec.WINDOWS, false, new char[] { '"', '*', ':', '<', '>', '?', '\\', '|', 0x7F }),
	WinForward(";", "/", DriveSpec.WINDOWS, false, new char[] { '"', '*', ':', '<', '>', '?', '\\', '|', 0x7F }),
	CygWin(":", "/", DriveSpec.CYGWIN, true, new char[] { '\000' }),
	UNIX(":", "/", DriveSpec.ROOTED, true, new char[] { '\000' });

	protected final String pathSeparator;

	protected final String fileSeparator;

	protected final DriveSpec driveSpec;

	protected final boolean caseSensitive;

	protected final char[] invalidCharacters;

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

	/**
	 * Sanitize file name removing invalid chars, and replacing them with URL-like escape sequences
	 *
	 * @param filename
	 * @return a file name without invalid chars
	 */
	public String sanitize(final String filename) {
		return sanitize(filename, invalidChar -> "%" + Integer.toHexString((int) invalidChar));
	}

	public String sanitize(final String filename, IFunction1<Character, String> replacement) {
		if (filename == null) return null;

		String retVal = filename;
		for (char invalidChar : getInvalidCharacters()) {
			retVal = retVal.replace(String.valueOf(invalidChar), replacement.apply(invalidChar));
		}
		return retVal;
	}

	public String sanitize(final String filename, String replacement) {
		return sanitize(filename, IFunction1.create(replacement));
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
			final String rootString = path.getRoot().toString(), modifiedRootString;
			final Platform platform = HPlatform.getPlatform();
			if (PlatformCategory.Microsoft.equals(platform.getCategory())) modifiedRootString = HString.stripSuffix(rootString, HPlatform.getPlatform().getPathSpec().getFileSeparator());
			else modifiedRootString = rootString;
			retVal.append(modifiedRootString);
		}
		for (int i = 0; i < path.getNameCount(); i++) {
			if (!retVal.isEmpty()) retVal.append(fileSeparator);
			retVal.append(path.getName(i).toString());
		}
		return retVal.toString();
	}
}
