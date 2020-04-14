package com.g2forge.alexandria.java.platform.cmdline.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.platform.cmdline.format.ICommandFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Win32CommandLineBuilder implements ICommandLineBuilder, ISingleton {
	@Getter
	@RequiredArgsConstructor
	public static enum SpecialCharacterSet {
		Ambiguous(" \t"),
		Script(" \t<>&|^"),
		Executable(" \t<>");

		protected final String specialCharacters;
	}

	protected static final Win32CommandLineBuilder INSTANCE = new Win32CommandLineBuilder();

	public static Win32CommandLineBuilder create() {
		return INSTANCE;
	}

	/**
	 * Determine if the Java runtime library loaded into this JVM will try to build Win32 command lines.
	 * 
	 * @return {@code true} if the current Java runtime library will try to build Win32 command lines.
	 */
	public static boolean isWin32ProcessBuilder() {
		final Class<?> processImplementationClass;
		try {
			processImplementationClass = Win32CommandLineBuilder.class.getClassLoader().loadClass("java.lang.ProcessImpl");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		final Set<String> fieldNames = Stream.of(processImplementationClass.getDeclaredFields()).map(f -> f.getName()).collect(Collectors.toSet());
		return fieldNames.contains("VERIFICATION_WIN32");
	}

	protected Win32CommandLineBuilder() {}

	/**
	 * {@inheritDoc}
	 * 
	 * Known issues correct on Win32:
	 * 
	 * <ol>
	 * <li>ProcessImpl adds quotes around arguments with spaces, but does not escape any characters inside the quotes.</li>
	 * <li>ProcessImpl assumes all commands require the same quoting and escaping rules which isn't necessarily true (e.g. echo under cmd.exe has different
	 * quoting rules).</li>
	 * </ol>
	 */
	@Override
	public List<String> build(ICommandFormat format, List<String> line) {
		/*if (isAllowAmbiguousCommands()) {
			// TODO: Implement
			throw new NotYetImplementedError();
		} else*/ {
			final String executable = line.get(0) /* TODO: I think the library normalizes the command for some reason?!? */;
			final SpecialCharacterSet specialCharacterSet = isScript(executable) ? SpecialCharacterSet.Script : SpecialCharacterSet.Executable;

			// TODO: Handle the exception case where getExecutablePath() forces a re-parsing of the command line (ew)

			final List<String> retVal = new ArrayList<>(line.size());
			retVal.add(executable);
			for (int i = 1; i < line.size(); i++) {
				final String argument = line.get(i);
				if (isQuoteRequired(specialCharacterSet, argument)) {
					retVal.add(format.quote(argument));
				} else retVal.add(argument);
			}
			return retVal;
		}
	}

	protected boolean isAllowAmbiguousCommands() {
		final SecurityManager security = System.getSecurityManager();
		if (security != null) return true;

		final String value = System.getProperty("jdk.lang.Process.allowAmbiguousCommands");
		if (value == null) return true;
		return !"false".equalsIgnoreCase(value);
	}

	protected boolean isQuoted(String string) {
		final int last = string.length() - 1;
		return (last >= 1) && (string.charAt(0) == '"') && (string.charAt(last) == '"');
	}

	public boolean isQuoteRequired(SpecialCharacterSet specialCharacterSet, String string) {
		if (isQuoted(string)) return false;
		final String specialCharacters = specialCharacterSet.getSpecialCharacters();
		for (int i = 0; i < string.length(); i++) {
			if (specialCharacters.indexOf(string.charAt(i)) >= 0) return true;
		}
		return false;
	}

	protected boolean isScript(String path) {
		final String uppercase = path.toUpperCase();
		return uppercase.endsWith(".CMD") || uppercase.endsWith(".BAT");
	}
}
