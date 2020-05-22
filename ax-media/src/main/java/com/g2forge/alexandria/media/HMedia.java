package com.g2forge.alexandria.media;

import java.nio.file.Path;

import com.g2forge.alexandria.java.core.marker.Helpers;

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
}
