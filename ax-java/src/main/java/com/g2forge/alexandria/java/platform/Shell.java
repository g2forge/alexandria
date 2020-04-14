package com.g2forge.alexandria.java.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Shell {
	// @formatter:off
	BASH(null,		null,																TextSpec.UNIX,	new String[] { "-c" },	PlatformCategory.Posix),
	TCSH(null,		null,																TextSpec.UNIX,	new String[] { "-c" },	PlatformCategory.Posix),
	ZSH(null,		null,																TextSpec.UNIX,	new String[] { "-c" },	PlatformCategory.Posix),
	CMD("CMD.EXE",	new ExecutableSpec[] { ExecutableSpec.BAT, ExecutableSpec.CMD },	TextSpec.DOS,	new String[] { "/C" },	PlatformCategory.Microsoft) {
		// @formatter:on
		@Override
		public List<String> wrapCommand(List<? extends String> arguments) {
			final String[] shellArguments = getArguments();
			final List<String> retVal = new ArrayList<>(shellArguments.length + 1 + arguments.size());
			retVal.add(((getName() == null) ? name().toLowerCase() : getName()));
			retVal.addAll(HCollection.asList(shellArguments));
			retVal.addAll(arguments);
			return retVal;
		}
	};

	protected final String name;

	protected final ExecutableSpec[] scripts;

	protected final TextSpec text;

	protected final String[] arguments;

	protected final PlatformCategory category;

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

	public List<String> wrapCommand(List<? extends String> arguments) {
		final String[] shellArguments = getArguments();
		final List<String> retVal = new ArrayList<>(shellArguments.length + 2);
		retVal.add(((getName() == null) ? name().toLowerCase() : getName()));
		retVal.addAll(HCollection.asList(shellArguments));
		retVal.add(arguments.stream().collect(Collectors.joining(" ")));
		return retVal;
	}
}
