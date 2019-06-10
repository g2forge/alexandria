package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.metadata.v5.IAnnotations;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MergedMetadata implements IAnnotatedMetadata {
	protected final Collection<? extends IAnnotations> components;

	public MergedMetadata(IAnnotatedMetadata... components) {
		this(Arrays.asList(components));
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		for (IAnnotations components : getComponents()) {
			final T retVal = components.getAnnotation(type);
			if (retVal != null) return retVal;
		}
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(final JavaScope scope) {
		return getComponents().stream().flatMap(components -> components.getAnnotations(scope).stream()).collect(Collectors.toList());
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		for (IAnnotations component : getComponents()) {
			if (component.isAnnotated(type)) return true;
		}
		return false;
	}
}
