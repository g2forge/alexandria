package com.g2forge.alexandria.filesystem.attributes;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HBasicFileAttributes {
	public static final String ATTRIBUTES_NAME = "basic";

	public enum Attribute {
		CreationTime,
		LastModifiedTime,
		LastAccessTime;

		@Getter(lazy = true)
		private final String attribute = name().substring(0, 1).toLowerCase() + name().substring(1);
	}

	public static FileAttribute<FileTime> createCreationTime(FileTime time) {
		return new SimpleFileAttribute<>(ATTRIBUTES_NAME, Attribute.CreationTime.getAttribute(), time);
	}

	public static FileAttribute<FileTime> createLastModifiedTime(FileTime time) {
		return new SimpleFileAttribute<>(ATTRIBUTES_NAME, Attribute.LastModifiedTime.getAttribute(), time);
	}

	public static FileAttribute<FileTime> createLastAccessedTime(FileTime time) {
		return new SimpleFileAttribute<>(ATTRIBUTES_NAME, Attribute.LastAccessTime.getAttribute(), time);
	}
}
