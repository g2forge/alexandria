package com.g2forge.alexandria.media;

import java.nio.file.Path;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMedia {
	public static IMediaType getMediaType(Path path) {
		return MediaType.getRegistry().computeMediaType(path);
	}

	public static IMediaType getMediaType(String filename) {
		return MediaType.getRegistry().computeMediaType(filename);
	}

	public static boolean isText(Path path) {
		final IMediaType mediaType = getMediaType(path);
		if (mediaType == null) return false;
		return mediaType.isText();
	}
}
