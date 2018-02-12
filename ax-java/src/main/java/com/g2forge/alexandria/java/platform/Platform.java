package com.g2forge.alexandria.java.platform;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Platform {
	NONE(null, null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	UNIX(null, PlatformCategory.Posix, Shell.BASH, LibSpec.SO, new ExeSpec[] { ExeSpec.None, ExeSpec.SH }, new UARTSpec[] { UARTSpec.ttyS, UARTSpec.ttyUSB }),
	WINDOWS(null, PlatformCategory.Microsoft, Shell.CMD, LibSpec.DLL, new ExeSpec[] { ExeSpec.EXE, ExeSpec.BAT }, new UARTSpec[] { UARTSpec.COM }),
	OS2("os/2", null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	ZOS("z/os", null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	OS400("os/400", null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	DOS(null, PlatformCategory.Microsoft, Shell.CMD, LibSpec.Unknown, new ExeSpec[] { ExeSpec.EXE, ExeSpec.BAT }, new UARTSpec[] { UARTSpec.COM }),
	MAC(null, null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	OSX(null, PlatformCategory.Posix, Shell.BASH, LibSpec.SO, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	TANDEM(null, null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	OPENVMS(null, null, Shell.BASH, LibSpec.Unknown, new ExeSpec[] { ExeSpec.Unknown }, new UARTSpec[] { UARTSpec.Unknown }),
	SOLARIS(null, PlatformCategory.Posix, Shell.BASH, LibSpec.SO, new ExeSpec[] { ExeSpec.None, ExeSpec.SH }, new UARTSpec[] { UARTSpec.ttyS, UARTSpec.ttyUSB });

	@Getter(lazy = true)
	private static final Platform platform = computePlatform();

	@Getter(lazy = true)
	private static final String[] envPath = computeEnvPath();

	protected static final String[] computeEnvPath() {
		final String path = Stream.of("PATH", "Path", "path").map(System::getenv).filter(Objects::nonNull).findFirst().get();
		return getPlatform().getPathSpec().splitPaths(path);
	}

	protected static Platform computePlatform() {
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("windows") != -1) return WINDOWS;
		if (os.indexOf("os/2") != -1) return OS2;
		if (os.indexOf("z/os") != -1 || os.indexOf("os/390") != -1) return ZOS;
		if (os.indexOf("os/400") != -1) return OS400;

		final String pathSeparator = System.getProperty("path.separator");
		if (pathSeparator.equals(";")) return DOS;
		if (os.indexOf("mac") != -1) {
			if (os.endsWith("x")) return OSX;
			else return MAC;
		}
		if (os.indexOf("nonstop_kernel") != -1) return TANDEM;
		if (os.indexOf("openvms") != -1) return OPENVMS;
		if (os.indexOf("solaris") != -1) return SOLARIS;
		if (pathSeparator.equals(":")) return UNIX;

		return NONE;
	}

	protected final String name;

	protected final PlatformCategory category;

	protected final Shell shell;

	/**
	 * The {@link PathSpec} of the default {@link Shell} when run on this platform. Note that this may not be the same as the {@link PathSpec} of the default
	 * {@link Shell} when run on the it's default platform.
	 */
	@Getter(lazy = true)
	private final PathSpec pathSpec = getShell().getPathSpec(this);

	protected final LibSpec libSpec;

	protected final ExeSpec[] exeSpecs;

	protected final UARTSpec[] uartSpecs;

	public Pattern getExePattern(String base) {
		final ExeSpec[] exeSpecs = getExeSpecs();

		final StringBuilder regex = new StringBuilder();
		regex.append("^(");
		regex.append(Pattern.quote(exeSpecs[0].getPlatformName(base)));
		for (int i = 1; i < exeSpecs.length; i++) {
			regex.append('|');
			regex.append(Pattern.quote(exeSpecs[i].getPlatformName(base)));
		}
		regex.append(")$");
		return Pattern.compile(regex.toString());
	}
}
