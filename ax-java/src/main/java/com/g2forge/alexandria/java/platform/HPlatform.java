package com.g2forge.alexandria.java.platform;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HPlatform {
	/**
	 * The {@link com.g2forge.alexandria.java.platform.Platform} representation of the platform on which this code is running.
	 * 
	 * @return The platform on which this code is running.
	 */
	@Getter(lazy = true)
	private static final Platform platform = computePlatform();

	/**
	 * The OS {@code PATH}, obtained from environment variables and properly converted to Java NIO paths.
	 * 
	 * @return The OS {@code PATH}.
	 */
	@Getter(lazy = true)
	private static final List<Path> path = computePath();

	protected static final List<Path> computePath() {
		final String path = Stream.of("PATH", "Path", "path").map(System::getenv).filter(Objects::nonNull).findFirst().get();
		return Stream.of(getPlatform().getPathSpec().splitPaths(path)).map(Paths::get).collect(Collectors.toList());
	}

	protected static Platform computePlatform() {
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("windows") != -1) return Platform.WINDOWS;
		if (os.indexOf("os/2") != -1) return Platform.OS2;
		if (os.indexOf("z/os") != -1 || os.indexOf("os/390") != -1) return Platform.ZOS;
		if (os.indexOf("os/400") != -1) return Platform.OS400;

		final String pathSeparator = System.getProperty("path.separator");
		if (pathSeparator.equals(";")) return Platform.DOS;
		if (os.indexOf("mac") != -1) {
			if (os.endsWith("x")) return Platform.OSX;
			else return Platform.MAC;
		}
		if (os.indexOf("nonstop_kernel") != -1) return Platform.TANDEM;
		if (os.indexOf("openvms") != -1) return Platform.OPENVMS;
		if (os.indexOf("solaris") != -1) return Platform.SOLARIS;
		if (pathSeparator.equals(":")) return Platform.UNIX;

		return Platform.NONE;
	}
}
