package com.g2forge.alexandria.command.process.cmdline;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HCommandLineBuilder {
	/**
	 * The {@link ICommandLineBuilder} to be used on the current Java runtime. This is decided based on the {@link java.lang.ProcessBuilder} implementation.
	 */
	@Getter(lazy = true)
	private static final ICommandLineBuilder commandLineBuilder = computeCommandLineBuilder();

	protected static ICommandLineBuilder computeCommandLineBuilder() {
		return Win32CommandLineBuilder.isWin32ProcessBuilder() ? Win32CommandLineBuilder.create() : NopCommandLineBuilder.create();
	}
}
