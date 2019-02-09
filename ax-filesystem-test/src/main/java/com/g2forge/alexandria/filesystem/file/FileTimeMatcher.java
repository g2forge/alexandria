package com.g2forge.alexandria.filesystem.file;

import java.nio.file.attribute.FileTime;
import java.time.Instant;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileTimeMatcher extends BaseMatcher<FileTime> {
	public static FileTime now() {
		return FileTime.from(Instant.now());
	}

	public static FileTimeMatcher.FileTimeMatcherBuilder start() {
		return FileTimeMatcher.builder().min(FileTimeMatcher.now());
	}

	protected final FileTime min;

	protected final FileTime max;

	public FileTimeMatcher(FileTime min, FileTime max) {
		if ((min != null) && (max != null) && (min.compareTo(max) > 0)) throw new IllegalArgumentException();
		if ((min == null) && (max == null)) throw new NullPointerException();
		this.min = min;
		this.max = max;
	}

	@Override
	public void describeTo(Description description) {
		if (getMin() == null) description.appendText("before ").appendValue(getMax());
		else if (getMax() == null) description.appendText("after ").appendValue(getMin());
		else {
			if (getMin().equals(getMax())) description.appendValue(getMin());
			else description.appendText("between ").appendValue(getMin()).appendText(" and ").appendValue(getMax());
		}
	}

	@Override
	public boolean matches(Object item) {
		final FileTime time = (FileTime) item;
		if ((getMin() != null) && (getMin().compareTo(time) > 0)) return false;
		if ((getMax() != null) && (getMax().compareTo(time) < 0)) return false;
		return true;
	}
}