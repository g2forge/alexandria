package com.g2forge.alexandria.media;

import java.nio.file.Path;

import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.io.Filename;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMedia {
	public static IMediaType getMediaType(String filename) {
		return MediaType.getRegistry().computeMediaType(new Filename(filename));
	}

	public static IMediaType getMediaType(Path path) {
		return MediaType.getRegistry().computeMediaType(new Filename(path));
	}

	public static boolean isText(Path path) {
		final IMediaType mediaType = getMediaType(path);
		if (mediaType == null) return false;
		return mediaType.isText();
	}
}
