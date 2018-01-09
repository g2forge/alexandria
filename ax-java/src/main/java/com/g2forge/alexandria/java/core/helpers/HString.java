package com.g2forge.alexandria.java.core.helpers;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HString {
	public static String lowercase(String string) {
		// Handle already lower-cased strings, we don't want to change them
		final char char0 = string.charAt(0);
		if (Character.isLowerCase(char0) || !Character.isLetter(char0)) return string;

		// Handle short strings, there's nothing more complex to do
		if (string.length() < 2) return string.toLowerCase();

		// Special logic so that we don't mess up acronyms and single-character prefixes
		final char char1 = string.charAt(1);
		if (Character.isUpperCase(char1)) {
			// If the second character is upper case, then we might not want to actually change the first character
			// For example if the string is only two characters (and they're both caps) then it's probably an acronym
			if (string.length() <= 2) return string.toLowerCase();
			// If the third character is not lowercase then the first three characters (at least) almost certainly form an acronym or prefix and we should
			// lowercase it together
			if (!Character.isLowerCase(string.charAt(2))) {
				int prefix = 3;
				for (; prefix < string.length(); prefix++) {
					if (Character.isLowerCase(string.charAt(prefix))) {
						prefix--;
						break;
					}
				}
				return string.substring(0, prefix).toLowerCase() + string.substring(prefix);
			}
		}
		return Character.toLowerCase(char0) + string.substring(1);
	}

	public static String nonWhiteSpace(String string) {
		return ((string == null) || (string.trim().length() < 1)) ? null : string;
	}

	public static String stripPrefix(String string, String... prefixes) {
		for (String prefix : prefixes) {
			if (string.startsWith(prefix)) return string.substring(prefix.length());
		}
		return string;
	}

	public static String stripSuffix(String string, String... suffixes) {
		for (String suffix : suffixes) {
			if (string.endsWith(suffix)) return string.substring(0, string.length() - suffix.length());
		}
		return string;
	}

	public static String unescape(String string) throws NotYetImplementedError {
		final StringBuilder retVal = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			final char current = string.charAt(i);
			if (current == '\\') {
				switch (string.charAt(i + 1)) {
					case '“':
					case '”':
					case '\\':
						retVal.append(string.charAt(i + 1));
						break;
					case 'n':
						retVal.append('\n');
						break;
					case 'r':
						retVal.append('\r');
						break;
					default:
						throw new NotYetImplementedError();
				}
				i++;
			} else retVal.append(current);
		}
		return retVal.toString();
	}
}
