package com.g2forge.alexandria.java.io;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HPath {
	/**
	 * Return a path in the specified filesystem which is equivalent to {@code path}. Maybe return {@code path} if it is already in the appropriate
	 * {@code fileSystem}.
	 * 
	 * @param fileSystem The target filesystem.
	 * @param path The path.
	 * @return A path with all the same components as {@code path} in the file system {@code fileSystem}.
	 */
	public static Path ensureFS(final FileSystem fileSystem, final Path path) {
		if (path.getFileSystem().equals(fileSystem)) return path;

		if (path.isAbsolute() && ((HCollection.toCollection(fileSystem.getRootDirectories()).size() > 1) || (HCollection.toCollection(path.getFileSystem().getRootDirectories()).size() > 1))) throw new NotYetImplementedError("Can't translate paths between filesystems when the root directories are complex!");
		Path current = fileSystem.getPath(path.isAbsolute() ? fileSystem.getSeparator() : "");
		// Resolve all the names, if there are any non-empty ones
		if ((path.getNameCount() != 1) || (path.getName(0).getFileName() != null)) {
			for (final Path name : path) {
				current = current.resolve(name.getFileName().toString());
			}
		}
		return current;
	}

	/**
	 * Test if the specified path is empty. An empty path is one with a single name, whose string value is empty (has zero characters). This is used to
	 * represent the default directory in a file system.
	 * 
	 * @param path The path to test.
	 * @return {@code true} if this path is empty.
	 */
	public static boolean isEmpty(Path path) {
		return path.getNameCount() == 1 && path.getName(0).toString().isEmpty();
	}

	public static Path replaceFilename(Path path, final String filename) {
		final Path parent = path.getParent();
		if ((parent == null) || (!path.isAbsolute() && (path.getNameCount() == 1))) return path.getFileSystem().getPath(filename);
		return parent.resolve(filename);
	}

	/**
	 * Resolve the {@code path} against the {@code base} after ensuring they're from the same filesystem.
	 * 
	 * @param base The base path.
	 * @param path The path to resolve against the base.
	 * @return The resolved path.
	 * @see Path#resolve(Path)
	 * @see #ensureFS(FileSystem, Path)
	 */
	public static Path resolveFS(Path base, Path path) {
		return base.resolve(ensureFS(base.getFileSystem(), path));
	}

	public static Path withBase(Path base, Path path) {
		if (path.isAbsolute() || (base == null)) return path;
		return base.resolve(path);
	}
}
