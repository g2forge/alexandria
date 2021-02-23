package com.g2forge.alexandria.java.io;

import java.nio.file.Path;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HPath {
	/**
	 * Get the file extension.
	 * 
	 * @param path The path to the file to get the extension of.
	 * @return The extension, including the period.
	 */
	public String getExtension(Path path) {
		return new Filename(path).getLastExtension();
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
}
