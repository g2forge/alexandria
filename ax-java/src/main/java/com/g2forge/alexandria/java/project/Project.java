package com.g2forge.alexandria.java.project;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Project {
	protected final Path root;
}