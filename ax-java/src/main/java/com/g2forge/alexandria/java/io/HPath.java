package com.g2forge.alexandria.java.io;

import java.nio.file.Path;

import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_O;
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

	public ITuple2G_<String, String> splitFilename(String filename) {
		final int index = filename.indexOf('.');
		if (index < 0) return new Tuple2G_O<>(filename, "");
		return new Tuple2G_O<>(filename.substring(0, index), filename.substring(index));
	}
}
