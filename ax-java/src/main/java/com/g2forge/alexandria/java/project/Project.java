package com.g2forge.alexandria.java.project;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Project {
	/**
	 * The root directory of this project, which may be the directory containing a {@code pom.xml} for example.
	 */
	protected final Path root;
}