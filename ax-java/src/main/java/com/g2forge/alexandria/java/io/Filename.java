package com.g2forge.alexandria.java.io;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
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
	public static Path replaceLastExtension(Path path, String extension) {
		final Path parent = path.getParent();
		final String modified = new Filename(path).getFullName() + "." + extension;
		return parent == null ? path.getFileSystem().getPath(modified) : parent.resolve(modified);
	}

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

	public String getFullExtensions() {
		final List<String> components = getComponents();
		final int size = components.size();
		if (size < 2) return null;
		return components.subList(1, size).stream().collect(Collectors.joining("."));
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

	public boolean isLastExtension(String extension, boolean caseSensitive) {
		final String lastExtension = getLastExtension();
		if ((lastExtension == null) || (extension == null)) return (lastExtension == null) && (extension == null);

		if (caseSensitive) return Objects.equals(extension, lastExtension);
		return Objects.equals(extension.toUpperCase(), lastExtension.toUpperCase());
	}

	@Override
	public String toString() {
		return getComponents().stream().collect(Collectors.joining("."));
	}
}
