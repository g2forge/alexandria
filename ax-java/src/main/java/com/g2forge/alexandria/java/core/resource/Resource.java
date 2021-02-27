package com.g2forge.alexandria.java.core.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Resource implements IResource {
	protected final Class<?> klass;

	protected final String resource;
}
