package com.g2forge.alexandria.filesystem.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.java.enums.EnumException;
import com.g2forge.alexandria.test.HAssert;

public class FileTimeTester implements Closeable {
	public static FileTimeTester all(Path path) {
		return new FileTimeTester(path, EnumSet.allOf(HBasicFileAttributes.Attribute.class));
	}

	protected static FileTime getTime(BasicFileAttributes attributes, HBasicFileAttributes.Attribute attribute) {
		switch (attribute) {
			case CreationTime:
				return attributes.creationTime();
			case LastAccessTime:
				return attributes.lastAccessTime();
			case LastModifiedTime:
				return attributes.lastModifiedTime();
			default:
				throw new EnumException(HBasicFileAttributes.Attribute.class, attribute);
		}
	}

	public static FileTimeTester modify(Path path) {
		return new FileTimeTester(path, HBasicFileAttributes.Attribute.LastModifiedTime, HBasicFileAttributes.Attribute.LastAccessTime);
	}

	public static FileTimeTester read(Path path) {
		return new FileTimeTester(path, HBasicFileAttributes.Attribute.LastAccessTime);
	}

	public static FileTimeTester untouched(Path path) {
		return new FileTimeTester(path);
	}

	protected final Path path;

	protected final EnumSet<HBasicFileAttributes.Attribute> changed;

	protected final FileTimeMatcher.FileTimeMatcherBuilder matcher;

	protected FileTimeTester(Path path, EnumSet<HBasicFileAttributes.Attribute> changed) {
		this.path = path;
		this.changed = changed;
		this.matcher = FileTimeMatcher.start();
	}

	public FileTimeTester(Path path, HBasicFileAttributes.Attribute... changed) {
		this.path = path;
		this.changed = EnumSet.noneOf(HBasicFileAttributes.Attribute.class);
		for (HBasicFileAttributes.Attribute change : changed) {
			this.changed.add(change);
		}
		this.matcher = FileTimeMatcher.start();
	}

	@Override
	public void close() throws IOException {
		final FileTimeMatcher changedMatcher = this.matcher.max(FileTimeMatcher.now()).build();
		final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

		for (HBasicFileAttributes.Attribute attribute : changed) {
			HAssert.assertThat(attribute + " time was not changed in correct range", getTime(attributes, attribute), changedMatcher);
		}

		final FileTimeMatcher unchangedMatcher = new FileTimeMatcher(null, changedMatcher.getMin());
		final EnumSet<HBasicFileAttributes.Attribute> unchanged = EnumSet.complementOf(changed);
		for (HBasicFileAttributes.Attribute attribute : unchanged) {
			HAssert.assertThat(attribute + " time was changed", getTime(attributes, attribute), unchangedMatcher);
		}
	}
}