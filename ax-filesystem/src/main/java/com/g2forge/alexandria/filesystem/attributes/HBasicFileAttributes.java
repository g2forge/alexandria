package com.g2forge.alexandria.filesystem.attributes;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HBasicFileAttributes {
	public static final String ATTRIBUTES_NAME = "basic";

	public enum Attribute {
		CreationTime,
		LastModifiedTime,
		LastAccessTime;

		public String getAttribute() {
			final String name = name();
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
	}

	public static FileAttribute<FileTime> createCreationTime(FileTime time) {
		return new SimpleFileAttribute<>(FileAttributeName.createName(ATTRIBUTES_NAME, Attribute.CreationTime.getAttribute()), time);
	}

	public static FileAttribute<FileTime> createLastModifiedTime(FileTime time) {
		return new SimpleFileAttribute<>(FileAttributeName.createName(ATTRIBUTES_NAME, Attribute.LastModifiedTime.getAttribute()), time);
	}

	public static FileAttribute<FileTime> createLastAccessedTime(FileTime time) {
		return new SimpleFileAttribute<>(FileAttributeName.createName(ATTRIBUTES_NAME, Attribute.LastAccessTime.getAttribute()), time);
	}
}
