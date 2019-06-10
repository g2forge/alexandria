package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.v5.IMetadata;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Metadata implements IMetadata {
	protected final AnnotatedElement element;

	protected final Object value;

	@TODO(value = "Implement support for loading instance specific metadata", link = "G2-469")
	public Metadata(AnnotatedElement element) {
		this(element, null);
	}

	public Metadata(AnnotatedElement element, Object value) {
		if ((element == null) && (value != null)) {
			this.element = (value instanceof Annotation) ? ((Annotation) value).annotationType() : value.getClass();
			this.value = value;
		} else {
			this.element = element;
			this.value = value;
		}
	}

	@Override
	public <T> T getMetadata(Class<T> type) {
		return IMetadataLoader.find(type).load(type, this);
	}

	@Override
	public boolean isMetadataPresent(Class<?> type) {
		return IMetadataLoader.find(type).isPresent(type, this);
	}
}
