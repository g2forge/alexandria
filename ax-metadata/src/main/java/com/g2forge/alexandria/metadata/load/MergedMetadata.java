package com.g2forge.alexandria.metadata.load;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.g2forge.alexandria.metadata.IMetadata;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotations;
import com.g2forge.alexandria.metadata.annotation.implementations.MergedJavaAnnotations;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MergedMetadata implements IAnnotatedMetadata {
	protected final Collection<? extends IMetadata> components;

	@Getter(lazy = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private final IJavaAnnotations annotations = new MergedJavaAnnotations(getComponents().stream().map(IAnnotatedMetadata.class::cast).map(IAnnotatedMetadata::getAnnotations).collect(Collectors.toList()));

	public MergedMetadata(IMetadata... components) {
		this(Arrays.asList(components));
	}
}
