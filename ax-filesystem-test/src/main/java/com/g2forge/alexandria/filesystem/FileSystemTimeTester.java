package com.g2forge.alexandria.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.filesystem.file.FileTimeTester;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.core.enums.HEnum;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FileSystemTimeTester {
	public static enum CreationTimeMode {
		/** Supports accurate creation times. */
		Supported,

		/** Does not support creation time. */
		Unsupported,

		/** Will return modified times instead of creation time. */
		Modified;
	}

	public static enum LastAccessTimeMode {
		/** Supports accurate last access times. */
		ATime,

		/** Does not support last access time at all. */
		NoATime,

		/** Linux relatime. Access time is updated only on writes, and every 24 hours. */
		RelATime;
	}

	public static final Pattern MICROSOFT_DISABLE_LAST_ACCESS_PATTERN = Pattern.compile("DisableLastAccess = ([0-9]+).*");

	/**
	 * Test if a Java standard file system for the specified <code>path</code> is expected to support creation time. Please do not use this method to test your
	 * own file system providers, where you should already know whether they support creation times.
	 * 
	 * 
	 * @param path The path to test for creation time support.
	 * @return The creation time mode for the file system underlying the specified path.
	 */
	public static CreationTimeMode getCreationTimeMode(final Path path) {
		final PlatformCategory category = HPlatform.getPlatform().getCategory();
		switch (category) {
			case Microsoft:
				return CreationTimeMode.Supported;
			case Posix:
				return CreationTimeMode.Modified;
			default:
				throw new EnumException(PlatformCategory.class, category);
		}
	}

	/**
	 * Test if a Java standard file system for the specified <code>path</code> is expected to support last access time. Specifically this method handles the
	 * fact that on Windows the NTFS file system makes access time optional, depending on a configuration setting. Please do not use this method to test your
	 * own file system providers, where you should already know whether they support last access times.
	 * 
	 * This method is conservative in that it expects all file systems to support last access time, unless we specifically know to the contrary. This way unit
	 * tests will fail if our assumptions are wrong, rather than passing when they should not.
	 * 
	 * @param path The path to test for last access time support.
	 * @return the last access time model for the file system underlying the specified path.
	 */
	public static LastAccessTimeMode getLastAccessTimeMode(final Path path) {
		final PlatformCategory category = HPlatform.getPlatform().getCategory();
		switch (category) {
			case Microsoft:
				if (!path.getFileSystem().supportedFileAttributeViews().contains("dos")) return LastAccessTimeMode.ATime;
				else {
					final ProcessBuilder builder = new ProcessBuilder();
					builder.command("powershell", "fsutil", "behavior", "query", "disablelastaccess");
					builder.directory(path.toFile());
					try {
						final Process process = builder.start();
						try {
							try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
								final Matcher matcher = MICROSOFT_DISABLE_LAST_ACCESS_PATTERN.matcher(reader.readLine().trim());
								if (!matcher.matches()) return LastAccessTimeMode.NoATime;
								final int value = Integer.valueOf(matcher.group(1));
								return ((value & 0x1) == 0) ? LastAccessTimeMode.ATime : LastAccessTimeMode.NoATime;
							}
						} finally {
							try {
								process.waitFor();
							} catch (InterruptedException e) {}
						}
					} catch (IOException e) {
						throw new RuntimeIOException(e);
					}
				}
			case Posix: {
				final ProcessBuilder builder = new ProcessBuilder();
				builder.command("bash", "-c", "cat /proc/mounts | grep \"^$(df --output=source . | tail -n 1)\" | cut -f 4 -d ' '");
				builder.directory(path.toFile());
				try {
					final Process process = builder.start();
					try {
						try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
							final Set<String> attributes = HCollection.asSet(reader.readLine().trim().split(","));
							final Optional<LastAccessTimeMode> optional = attributes.stream().map(attribute -> {
								try {
									return HEnum.valueOfInsensitive(LastAccessTimeMode.class, attribute);
								} catch (Throwable throwable) {
									return null;
								}
							}).filter(Objects::nonNull).collect(HCollector.toOptional());
							return optional.orElse(LastAccessTimeMode.RelATime);
						}
					} finally {
						try {
							process.waitFor();
						} catch (InterruptedException e) {}
					}
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			}
			default:
				throw new EnumException(PlatformCategory.class, category);
		}
	}

	protected final FileSystemTimeTester.CreationTimeMode creationTimeMode;

	protected final FileSystemTimeTester.LastAccessTimeMode lastAccessTimeMode;

	public FileSystemTimeTester() {
		this(FileSystemTimeTester.CreationTimeMode.Modified, FileSystemTimeTester.LastAccessTimeMode.ATime);
	}

	public FileSystemTimeTester(Path path) {
		this(getCreationTimeMode(path), getLastAccessTimeMode(path));
	}

	public FileTimeTester all(Path path) {
		return new FileTimeTester(path, EnumSet.allOf(HBasicFileAttributes.Attribute.class));
	}

	public FileTimeTester createModify(Path path) {
		return new FileTimeTester(path, EnumSet.allOf(HBasicFileAttributes.Attribute.class));
	}

	public FileTimeTester modify(Path path) {
		final HBasicFileAttributes.Attribute access = FileSystemTimeTester.LastAccessTimeMode.ATime.equals(getLastAccessTimeMode()) ? HBasicFileAttributes.Attribute.LastAccessTime : null;
		final HBasicFileAttributes.Attribute creation = FileSystemTimeTester.CreationTimeMode.Modified.equals(getCreationTimeMode()) ? HBasicFileAttributes.Attribute.CreationTime : null;
		return new FileTimeTester(path, HBasicFileAttributes.Attribute.LastModifiedTime, access, creation);
	}

	public FileTimeTester read(Path path) {
		final HBasicFileAttributes.Attribute access = !FileSystemTimeTester.LastAccessTimeMode.NoATime.equals(getLastAccessTimeMode()) ? HBasicFileAttributes.Attribute.LastAccessTime : null;
		return new FileTimeTester(path, access);
	}

	public FileTimeTester untouched(Path path) {
		return new FileTimeTester(path);
	}
}