package com.g2forge.alexandria.command.clireport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseableSupplier;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.platform.HPlatform;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class HCLIReport {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Output {
		protected final int exitCode;

		protected final List<String> output;
	}

	public static final String CLIREPORT_VERSION = "v0.0.3";

	public static final String CLIREPORT_FILENAME = "clireport";

	public static final String CLIREPORT_DOWNLOADFORMAT = "https://github.com/g2forge/clireport/releases/download/%1$s/%2$s";

	public static Output computeExpectedOutput(final List<String> arguments) {
		final List<String> output = new ArrayList<>();
		output.add(String.format("CLIReport: %1$d arguments", arguments.size()));
		output.addAll(arguments.stream().map(argument -> String.format("%1$04d: %2$s", argument.length(), argument)).collect(Collectors.toList()));

		Integer exitCode = null;
		if ((arguments.size() > 2) && ("--exit".equals(arguments.get(1)))) {
			try {
				exitCode = Integer.parseInt(arguments.get(2));
			} catch (NumberFormatException exception) {}
			if (exitCode != null) output.add(String.format("Exit code \"%s\" (%d) is valid", arguments.get(2), exitCode));
			else {
				output.add(String.format("Exit code \"%s\" is not valid, using -1 instead", arguments.get(2)));
				exitCode = -1;
			}
		} else {
			output.add("Exit code was not specified");
			exitCode = 0;
		}

		return new Output(exitCode, output);
	}

	public static Output computeExpectedOutput(String executable, String... arguments) {
		return computeExpectedOutput(HCollection.concatenate(HCollection.asList(executable), HCollection.asList(arguments)));
	}

	public static ICloseableSupplier<Path> download(Path directory) {
		final String filename = HPlatform.getPlatform().getExeSpecs()[0].fromBase(CLIREPORT_FILENAME);
		final Path path = directory == null ? Paths.get(filename) : directory.resolve(filename);
		if (!Files.exists(path)) {
			final String url = String.format(CLIREPORT_DOWNLOADFORMAT, CLIREPORT_VERSION, path.getFileName().toString());
			try (final InputStream input = new URL(url).openStream();
				final OutputStream output = Files.newOutputStream(path)) {
				HBinaryIO.copy(input, output);
			} catch (IOException e) {
				throw new RuntimeIOException("Failed to download clireport", e);
			}
			if (!Files.exists(path)) throw new RuntimeException(String.format("Failed to download %1$s to %2$s", url, path));
			try {
				Files.setPosixFilePermissions(path, EnumSet.allOf(PosixFilePermission.class));
			} catch (UnsupportedOperationException exception) {
				// Ignore this - it's not required on platforms where it's not supported
			} catch (IOException exception) {
				throw new RuntimeIOException(String.format("Failed to mark %1$s executable", path), exception);
			}
			if (!Files.isExecutable(path)) throw new RuntimeException(String.format("%1$s is not executable", path));
		}
		return new ICloseableSupplier<Path>() {
			@Override
			public void close() {
				try {
					Files.deleteIfExists(path);
				} catch (IOException e) {
					throw new RuntimeException("Failed to delete downloaded " + CLIREPORT_FILENAME, e);
				}
			}

			@Override
			public Path get() {
				return path;
			}
		};
	}
}
