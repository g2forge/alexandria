package com.g2forge.alexandria.java.project;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Location {
	protected final Project project;

	protected final Path source;

	protected final Path resources;

	protected final Path target;
}