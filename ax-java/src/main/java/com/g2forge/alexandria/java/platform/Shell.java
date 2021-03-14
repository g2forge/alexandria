package com.g2forge.alexandria.java.platform;

import com.g2forge.alexandria.java.core.enums.EnumException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Shell {
	// @formatter:off
	BASH(null,			null,																TextSpec.UNIX,	new String[] { "-c" },			PlatformCategory.Posix,		VariableSpec.POSIX),
	TCSH(null,			null,																TextSpec.UNIX,	new String[] { "-c" },			PlatformCategory.Posix,		VariableSpec.POSIX),
	ZSH(null,			null,																TextSpec.UNIX,	new String[] { "-c" },			PlatformCategory.Posix,		VariableSpec.POSIX),
	CMD("CMD.EXE",		new ExecutableSpec[] { ExecutableSpec.BAT, ExecutableSpec.CMD },	TextSpec.DOS,	new String[] { "/C" },			PlatformCategory.Microsoft,	VariableSpec.CMD),
	POWERSHELL(null,	new ExecutableSpec[] { ExecutableSpec.PS1 },						TextSpec.DOS,	new String[] { "-Command" },	PlatformCategory.Microsoft,	VariableSpec.POWERSHELL);
	//@formatter:on

	protected final String name;

	protected final ExecutableSpec[] scripts;

	protected final TextSpec text;

	protected final String[] arguments;

	protected final PlatformCategory category;

	protected final VariableSpec variable;

	/**
	 * Get the path specification to use with this shell on the given platform.
	 * 
	 * @param platform Can be {@code null} to indicate the current platform.
	 * @return The path specification to use with this shell.
	 */
	public PathSpec getPath(Platform platform) {
		if (platform == null) platform = HPlatform.getPlatform();

		final PlatformCategory category = getCategory();
		switch (category) {
			case Microsoft:
				return PathSpec.WinBack;
			case Posix:
				if (platform.getCategory() == null) return PathSpec.UNIX;
				switch (platform.getCategory()) {
					case Microsoft:
						return PathSpec.CygWin;
					case Posix:
						return PathSpec.UNIX;
				}
				throw new IllegalArgumentException("Platform \"" + platform + "\" is not known, specifically the category (\"" + platform.getCategory() + "\") must be known to find the path spec for native " + category + " shells!");
			default:
				throw new EnumException(PlatformCategory.class, category);
		}
	}
}
