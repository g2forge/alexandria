package com.g2forge.alexandria.media;

import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
public class SimpleFileExtensions implements IFileExtensions {
	public static class SimpleFileExtensionsBuilder implements IBuilder<SimpleFileExtensions> {
		public SimpleFileExtensionsBuilder defaultExtension(String defaultExtension) {
			this.defaultExtension = defaultExtension;
			this.extension(defaultExtension);
			return this;
		}
	}

	protected final String defaultExtension;

	@Singular
	protected final Set<String> extensions;

	public SimpleFileExtensions(String defaultExtension, Set<String> extensions) {
		this.defaultExtension = defaultExtension.toLowerCase();
		this.extensions = extensions.stream().map(String::toLowerCase).collect(Collectors.toSet());
		if (!getExtensions().contains(getDefaultExtension())) throw new IllegalArgumentException("Extensions must include default extension!");
	}

	public SimpleFileExtensions(String defaultExtension, String... extensions) {
		this(defaultExtension, HCollection.union(HCollection.asSet(defaultExtension), HCollection.asSet(extensions)));
	}

	@Override
	public boolean isMatch(String extension) {
		return getExtensions().contains(extension.toLowerCase());
	}
}
