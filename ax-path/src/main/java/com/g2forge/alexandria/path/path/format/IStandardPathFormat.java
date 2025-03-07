package com.g2forge.alexandria.path.path.format;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.collection.CollectionCollection;
import com.g2forge.alexandria.path.path.IPath;

public interface IStandardPathFormat<T, P extends IPath<T>> extends IPathFormat<T, P> {
	public String getSeparator();

	public default String getSeparatorPattern() {
		return Pattern.quote(getSeparator());
	}

	@Override
	public default P toPath(String path) {
		final String[] componentsArray = path.split(getSeparatorPattern());
		final List<T> componentsList = Stream.of(componentsArray).map(this::toComponent).collect(Collectors.toList());
		return toPath(new CollectionCollection<>(componentsList));
	}

	@Override
	public default String toString(IPath<T> path) {
		return path.getComponents().stream().map(this::toString).collect(Collectors.joining(getSeparator()));
	}
}
