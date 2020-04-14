package com.g2forge.alexandria.java.platform.cmdline.builder;

import java.util.List;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.platform.cmdline.format.ICommandFormat;

public class NopCommandLineBuilder implements ICommandLineBuilder, ISingleton {
	protected static final NopCommandLineBuilder INSTANCE = new NopCommandLineBuilder();

	public static NopCommandLineBuilder create() {
		return INSTANCE;
	}

	protected NopCommandLineBuilder() {}

	@Override
	public List<String> build(ICommandFormat format, List<String> line) {
		return line;
	}
}
