package com.g2forge.alexandria.filesystem.attributes;

import java.nio.file.attribute.FileAttribute;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class SimpleFileAttribute<T> implements FileAttribute<T> {
	protected final String name;

	protected final T value;

	public SimpleFileAttribute(String attributes, String attribute, T value) {
		this(FileAttributeName.createName(attributes, attribute), value);
	}

	@Override
	public String name() {
		return getName();
	}

	@Override
	public T value() {
		return getValue();
	}
}
