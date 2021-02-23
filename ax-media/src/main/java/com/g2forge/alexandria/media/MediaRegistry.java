package com.g2forge.alexandria.media;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MediaRegistry implements IMediaRegistry {
	@Singular
	protected final Collection<IMediaType> mediaTypes;

	public MediaRegistry(IMediaType... mediaTypes) {
		this(HCollection.asList(mediaTypes));
	}
}
