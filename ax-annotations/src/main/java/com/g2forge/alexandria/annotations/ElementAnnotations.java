package com.g2forge.alexandria.annotations;

import java.util.function.Supplier;

import javax.lang.model.element.Element;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ElementAnnotations<T> {
	protected final Element element;

	protected final Supplier<String> path;

	protected final Class<? extends T> type;

	protected final T[] annotations;
}
