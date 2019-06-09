package com.g2forge.alexandria.metadata.v5;

import java.lang.reflect.AnnotatedElement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AnnotatedElementElement implements IElement {
	protected final AnnotatedElement element;

	@Override
	public <_T> _T apply(Class<_T> type) {
		return HMetadata.findLoader(type).load(this);
	}
}
