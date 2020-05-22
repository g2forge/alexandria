package com.g2forge.alexandria.media;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.Filename;

public interface IMediaRegistry {
	public default IMediaType computeMediaType(Filename filename) {
		final String extension = filename.getLastExtension();
		final List<IMediaType> matches = getMediaTypes().stream().filter(mediaType -> mediaType.getFileExtensions().isMatch(extension)).collect(Collectors.toList());
		return HCollection.getOne(matches);
	}

	public Collection<IMediaType> getMediaTypes();
}
