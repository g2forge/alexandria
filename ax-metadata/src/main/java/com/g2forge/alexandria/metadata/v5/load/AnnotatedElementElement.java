package com.g2forge.alexandria.metadata.v5.load;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.metadata.v5.IMetadata;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class AnnotatedElementElement implements IMetadata {
	protected final AnnotatedElement element;

	@Override
	public <T> T getMetadata(Class<T> type) {
		return IMetadataLoader.find(type).load(type, this);
	}
}
