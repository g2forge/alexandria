package com.g2forge.alexandria.path.path.filename;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.path.path.format.IStringPathFormat;

public class FilenamePathFormat implements IStringPathFormat<Filename>, ISingleton {
	protected static final FilenamePathFormat INSTANCE = new FilenamePathFormat();

	public static FilenamePathFormat create() {
		return INSTANCE;
	}

	protected FilenamePathFormat() {}

	@Override
	public String getSeparator() {
		return ".";
	}

	@Override
	public Filename toPath(ICollection<String> components) {
		return new Filename(components);
	}
}
