package com.g2forge.alexandria.java.project;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Location {
	/** The layout of this code location. See {@link Layout} for more information. */
	protected final Layout layout;

	/** The java project under which this code location exists. */
	protected final Project project;

	/** The directory in which java source files exist. May be {@code null} if this location is a JAR or otherwise has no source directory. */
	protected final Path source;

	/** The directory in which java resource files exist. May be {@code null} if this location is a JAR or otherwise has no source location. */
	protected final Path resources;

	/**
	 * The directory in which the outputs of the compilation process (source and resources) exist. May be {@code null}, or the directory containing the JAR file
	 * if this location denotes a JAR.
	 */
	protected final Path target;

	/**
	 * The JAR file, if this location is a JAR. {@code null} otherwise.
	 * 
	 * @see Layout#isJar()
	 */
	protected final Path jar;
}