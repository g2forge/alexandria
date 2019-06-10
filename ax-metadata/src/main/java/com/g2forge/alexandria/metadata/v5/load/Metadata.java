package com.g2forge.alexandria.metadata.v5.load;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.v5.IMetadata;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Metadata implements IMetadata {
	protected final AnnotatedElement element;

	protected final Object value;

	@TODO(value = "Implement support for loading instance specific metadata", link = "G2-469")
	public Metadata(AnnotatedElement element) {
		this(element, null);
	}

	@Override
	public <T> T getMetadata(Class<T> type) {
		return IMetadataLoader.find(type).load(type, this);
	}
}
