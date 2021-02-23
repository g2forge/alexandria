package com.g2forge.alexandria.media;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CompositeMediaRegistry implements IMediaRegistry {
	@Singular("registry")
	protected final List<IMediaRegistry> registries;

	@Getter(lazy = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private final Collection<IMediaType> mediaTypes = getRegistries().stream().map(IMediaRegistry::getMediaTypes).flatMap(Collection::stream).collect(Collectors.toList());

	public CompositeMediaRegistry(IMediaRegistry... registries) {
		this(HCollection.asList(registries));
	}
}
