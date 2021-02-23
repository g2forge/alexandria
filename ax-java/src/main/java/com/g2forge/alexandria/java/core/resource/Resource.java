package com.g2forge.alexandria.java.core.resource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Resource implements IResource {
	protected final Class<?> klass;

	protected final String resource;

	public Path getPath() {
		try {
			return Paths.get(getKlass().getResource(getResource()).toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
