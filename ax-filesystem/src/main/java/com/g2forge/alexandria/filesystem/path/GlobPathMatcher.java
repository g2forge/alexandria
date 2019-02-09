package com.g2forge.alexandria.filesystem.path;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.regex.Pattern;

/**
 * A standard implementation of {@link PathMatcher} suitable for use by {@link java.nio.file.FileSystem#getPathMatcher(String)}. This class is implemented by
 * compiling a glob into a regular expression and then matching is performed over the string representation of paths.
 */
public class GlobPathMatcher implements PathMatcher {
	protected final Pattern pattern;

	public GlobPathMatcher(String pattern, String separator) {
		if (separator.length() != 1) throw new UnsupportedOperationException("Glob matcher supports path separators of exactly one character.");

		final char[] characters = pattern.toCharArray();
		final StringBuilder builder = new StringBuilder(pattern.length());
		int groupNesting = 0, classNesting = 0, startOfClass = -1;
		for (int i = 0; i < characters.length; i++) {
			final char c = characters[i];
			switch (c) {
				// Basic glob patterns
				case '?':
					// If we're not in a class, then match non-separator characters
					if (classNesting == 0) builder.append("[^").append(Pattern.quote(separator)).append("]");
					else builder.append('?');
					break;
				case '*':
					// If we're not in a class
					if (classNesting == 0) {
						if (i + 1 < characters.length) {
							final char next = characters[i + 1];
							if (next == '*') {
								// Match any characters
								builder.append(".*");
								break;
							}
						}

						// Match non-separator characters
						builder.append("[^").append(Pattern.quote(separator)).append("]*");
					} else builder.append('*');
					break;

				// Handle escape characters
				case '\\':
					if (++i >= characters.length) builder.append('\\');
					else {
						final char next = characters[i];
						switch (next) {
							case 'Q': // Regex quote characters, need to be escaped more
							case 'E':
								builder.append("\\\\");
								break;
							case ',': // Doesn't need to be escaped at all
								break;
							default: // Normal escaping
								builder.append('\\');
								break;
						}
						builder.append(next);
					}
					break;

				// Handle classes
				case '[':
					classNesting++;
					builder.append('[');
					startOfClass = i + 1;
					break;
				case ']':
					classNesting--;
					builder.append(']');
					break;
				case '.':
				case '(':
				case ')':
				case '+':
				case '|':
				case '^':
				case '$':
				case '@':
				case '%':
					if ((classNesting == 0) || ((startOfClass == i) && (c == '^'))) builder.append('\\');
					builder.append(c);
					break;
				case '!':
					if (startOfClass == i) builder.append('^');
					else builder.append('!');
					break;

				// Handle groups
				case '{':
					groupNesting++;
					builder.append('(');
					break;
				case '}':
					groupNesting--;
					builder.append(')');
					break;
				case ',':
					if (groupNesting > 0) builder.append('|');
					else builder.append(',');
					break;

				default:
					builder.append(c);
					break;
			}
		}

		this.pattern = Pattern.compile(builder.toString());
	}

	@Override
	public boolean matches(Path path) {
		return pattern.matcher(path.toString()).matches();
	}
}