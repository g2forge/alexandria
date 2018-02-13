package com.g2forge.alexandria.java.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.enums.EnumException;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Shell {
	BASH(null, null, TXTSpec.UNIX, new String[] { "-c" }, PlatformCategory.Posix),
	CMD("CMD.EXE", new ExeSpec[] { ExeSpec.BAT }, TXTSpec.DOS, new String[] { "/C" }, PlatformCategory.Microsoft) {
		@Override
		public IFunction1<? super List<? extends String>, ? extends List<? extends String>> getCommandNesting() {
			return arguments -> {
				final String[] shellArguments = getArguments();
				final List<String> retVal = new ArrayList<>(arguments.size() + 1 + shellArguments.length);
				retVal.add(((getName() == null) ? name() : getName()).toLowerCase());
				retVal.addAll(HCollection.asList(shellArguments));
				retVal.addAll(arguments);
				return retVal;
			};
		}
	},
	TCSH(null, null, TXTSpec.UNIX, new String[] { "-c" }, PlatformCategory.Posix);

	protected final String name;

	protected final ExeSpec[] scripts;

	protected final TXTSpec text;

	protected final String[] arguments;

	protected final PlatformCategory category;

	public IFunction1<? super List<? extends String>, ? extends List<? extends String>> getCommandNesting() {
		return arguments -> {
			final String[] shellArguments = getArguments();
			final List<String> retVal = new ArrayList<>(shellArguments.length + 2);
			retVal.add(((getName() == null) ? name() : getName()).toLowerCase());
			retVal.addAll(HCollection.asList(shellArguments));
			retVal.add(arguments.stream().collect(Collectors.joining(" ")));
			return retVal;
		};
	}

	public PathSpec getPathSpec() {
		return getPathSpec(null);
	}

	/**
	 * Get the path specification to use with this shell on the given platform.
	 * 
	 * @param platform Can be <code>null</code> to indicate the current platform.
	 * @return The path specification to use with this shell.
	 */
	public PathSpec getPathSpec(Platform platform) {
		if (platform == null) platform = Platform.getPlatform();

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
