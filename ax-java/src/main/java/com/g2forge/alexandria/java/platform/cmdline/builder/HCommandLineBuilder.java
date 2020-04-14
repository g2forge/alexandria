package com.g2forge.alexandria.java.platform.cmdline.builder;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HCommandLineBuilder {
	@Getter(lazy = true)
	private static final ICommandLineBuilder commandLineBuilder = computeCommandLineBuilder();

	protected static ICommandLineBuilder computeCommandLineBuilder() {
		return Win32CommandLineBuilder.isWin32ProcessBuilder() ? Win32CommandLineBuilder.create() : NopCommandLineBuilder.create();
	}
}
