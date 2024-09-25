package com.g2forge.alexandria.media;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.path.path.filename.Filename;

public interface IMediaRegistry {
	public default IMediaType computeMediaType(Filename filename) {
		final String extension = filename.getLast();
		return HStream.findOneOptional(getMediaTypes().stream().filter(mediaType -> mediaType.getFileExtensions().isMatch(extension)), () -> null);
	}

	public default IMediaType computeMediaType(Path filename) {
		return computeMediaType(Filename.fromPath(filename));
	}

	public default IMediaType computeMediaType(String filename) {
		return computeMediaType(Filename.fromString(filename));
	}

	public Collection<IMediaType> getMediaTypes();

	public default boolean isMediaType(IMediaType expected, Filename filename) {
		return Objects.equals(expected, computeMediaType(filename));
	}

	public default boolean isMediaType(IMediaType expected, Path filename) {
		return Objects.equals(expected, computeMediaType(filename));
	}

	public default boolean isMediaType(IMediaType expected, String filename) {
		return Objects.equals(expected, computeMediaType(filename));
	}
}
