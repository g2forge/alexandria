package com.g2forge.alexandria.filesystem.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.test.HAssert;

public class FileTimeTester implements Closeable {
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

	protected final Path path;

	protected final EnumSet<HBasicFileAttributes.Attribute> changed;

	protected final FileTimeMatcher.FileTimeMatcherBuilder matcher;

	public FileTimeTester(Path path, EnumSet<HBasicFileAttributes.Attribute> changed) {
		this.path = path;
		this.changed = changed;
		this.matcher = FileTimeMatcher.start();
		delayForMismatch();
	}

	public FileTimeTester(Path path, HBasicFileAttributes.Attribute... changed) {
		this.path = path;
		this.changed = EnumSet.noneOf(HBasicFileAttributes.Attribute.class);
		for (HBasicFileAttributes.Attribute change : changed) {
			if (change != null) this.changed.add(change);
		}
		this.matcher = FileTimeMatcher.start();
		delayForMismatch();
	}

	protected void assertTimeAttribute(final FileTimeMatcher changedMatcher, final FileTimeMatcher unchangedMatcher, HBasicFileAttributes.Attribute attribute, final boolean changed, final FileTime time) {
		if (changed) HAssert.assertThat(attribute + " time on " + path + " was not changed in correct range", time, changedMatcher);
		else HAssert.assertThat(attribute + " time on " + path + " was changed when it shouldn't have", time, unchangedMatcher);
	}

	@Override
	public void close() throws IOException {
		delayForMismatch();
		final FileTimeMatcher changedMatcher = this.matcher.max(FileTimeMatcher.now()).build();
		final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

		final FileTimeMatcher unchangedMatcher = new FileTimeMatcher(null, changedMatcher.getMin());
		for (HBasicFileAttributes.Attribute attribute : HBasicFileAttributes.Attribute.values()) {
			final boolean changed = this.changed.contains(attribute);
			final FileTime time = getTime(attributes, attribute);
			assertTimeAttribute(changedMatcher, unchangedMatcher, attribute, changed, time);
		}
	}

	/**
	 * Delay our operation to allow for mismatches between java time and file system time.
	 */
	protected void delayForMismatch() {
		HConcurrent.wait(10);
	}
}