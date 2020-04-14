package com.g2forge.alexandria.java.platform;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Platform {
	// @formatter:off
	NONE(	null,		null, 						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	UNIX(	null,		PlatformCategory.Posix,		Shell.BASH,	LibrarySpec.SO,			new ExecutableSpec[] { ExecutableSpec.None, ExecutableSpec.SH },						new UARTSpec[] { UARTSpec.ttyS, UARTSpec.ttyUSB }),
	WINDOWS(null,		PlatformCategory.Microsoft,	Shell.CMD,	LibrarySpec.DLL,		new ExecutableSpec[] { ExecutableSpec.EXE, ExecutableSpec.BAT, ExecutableSpec.CMD },	new UARTSpec[] { UARTSpec.COM }),
	OS2(	"os/2",		null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	ZOS(	"z/os",		null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	OS400(	"os/400",	null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	DOS(	null, 		PlatformCategory.Microsoft,	Shell.CMD,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.EXE, ExecutableSpec.BAT },						new UARTSpec[] { UARTSpec.COM }),
	MAC(	null,		null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	OSX(	null, 		PlatformCategory.Posix,		Shell.ZSH,	LibrarySpec.SO,			new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }){
		// @formatter:on
		@Override
		public Shell getShell() {
			final String[] version = System.getProperty("os.version").split("\\.");
			final int major = Integer.valueOf(version[0]);
			if (major < 10) throw new UnsupportedOperationException("OSX is version 10 or later");
			if (major == 10) {
				final int minor = Integer.valueOf(version[1]);
				if (minor < 3) return Shell.TCSH;
				if (minor < 15) return Shell.BASH;
			}
			return Shell.ZSH;
		}
	// @formatter:off
	},
	TANDEM(	null,		null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	OPENVMS(null, 		null,						Shell.BASH,	LibrarySpec.Unknown,	new ExecutableSpec[] { ExecutableSpec.Unknown },										new UARTSpec[] { UARTSpec.Unknown }),
	SOLARIS(null, 		PlatformCategory.Posix,		Shell.BASH,	LibrarySpec.SO,			new ExecutableSpec[] { ExecutableSpec.None, ExecutableSpec.SH },						new UARTSpec[] { UARTSpec.ttyS, UARTSpec.ttyUSB });
	// @formatter:on

	protected final String name;

	protected final PlatformCategory category;

	protected final Shell shell;

	/**
	 * The {@link PathSpec} of the default {@link Shell} when run on this platform. Note that this may not be the same as the {@link PathSpec} of the default
	 * {@link Shell} when run on the it's default platform.
	 * 
	 * @return The path specification for the default shell when run on this platform.
	 */
	@Getter(lazy = true)
	private final PathSpec pathSpec = getShell().getPath(this);

	protected final LibrarySpec libSpec;

	protected final ExecutableSpec[] exeSpecs;

	protected final UARTSpec[] uartSpecs;

	public Pattern getExecutablePattern(String name) {
		final ExecutableSpec[] specs = getExeSpecs();

		final StringBuilder regex = new StringBuilder();
		regex.append("^(");
		regex.append(Pattern.quote(specs[0].getPlatformName(name)));
		for (int i = 1; i < specs.length; i++) {
			regex.append('|');
			regex.append(Pattern.quote(specs[i].getPlatformName(name)));
		}
		regex.append(")$");
		return Pattern.compile(regex.toString());
	}
}
