package com.g2forge.alexandria.path.path.format;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

public interface IStandardPathFormat<T> extends IPathFormat<T> {
	public String getSeparator();

	@Override
	public default IPath<T> toPath(String path) {
		final String[] components = path.split(Pattern.quote(getSeparator()));
		return new Path<>(Stream.of(components).map(this::toComponent).collect(Collectors.toList()));
	}

	@Override
	public default String toString(IPath<T> path) {
		return path.getComponents().stream().map(this::toString).collect(Collectors.joining(getSeparator()));
	}
}
