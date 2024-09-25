package com.g2forge.alexandria.path.path.format;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OSPathFormat implements IStringPathFormat<IPath<String>> {
	Microsoft("\\"),
	POSIX("/");

	protected final String separator;

	@Override
	public IPath<String> toPath(ICollection<String> components) {
		return new Path<>(components);
	}
}
