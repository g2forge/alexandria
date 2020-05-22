package com.g2forge.alexandria.media;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Filename {
	@Singular
	protected final List<String> components;

	public Filename(Path path) {
		this(path.getFileName().toString());
	}

	public Filename(String string) {
		this(HCollection.asList(string.split("\\.")));
	}

	public String getFirstName() {
		final List<String> components = getComponents();
		if (components.isEmpty()) return null;
		return components.get(0);
	}

	public String getFullName() {
		final List<String> components = getComponents();
		final int size = components.size();
		final List<String> sublist = size > 1 ? components.subList(0, size - 1) : components;
		return sublist.stream().collect(Collectors.joining("."));
	}

	public String getLastExtension() {
		final List<String> components = getComponents();
		if (components.size() < 2) return null;
		return components.get(components.size() - 1);
	}

	@Override
	public String toString() {
		return getComponents().stream().collect(Collectors.joining("."));
	}
}
