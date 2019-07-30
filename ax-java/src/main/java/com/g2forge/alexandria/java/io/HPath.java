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
		final String string = path.getFileName().toString();
		final int index = string.indexOf('.');
		if (index < 0) return "";
		return string.substring(index);
	}
}
