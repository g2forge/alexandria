package com.g2forge.alexandria.annotations;

import java.util.function.Supplier;

import javax.lang.model.element.Element;

public class ElementAnnotations<T> {
	protected final Element element;

	protected final Supplier<String> path;

	protected final Class<? extends T> type;

	protected final T[] annotations;

	public ElementAnnotations(Element element, Supplier<String> path, Class<? extends T> type, T[] annotations) {
		this.element = element;
		this.path = path;
		this.type = type;
		this.annotations = annotations;
	}

	public T[] getAnnotations() {
		return annotations;
	}

	public Element getElement() {
		return element;
	}

	public Supplier<String> getPath() {
		return path;
	}

	public Class<? extends T> getType() {
		return type;
	}
}
