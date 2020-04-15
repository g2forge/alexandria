package com.g2forge.alexandria.java.platform.cmdline.builder;

import java.util.List;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.platform.cmdline.format.ICommandFormat;

/**
 * {@inheritDoc}
 * 
 * Work around all the poor quoting and escaping mistakes in
 * <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/solaris/classes/java/lang/ProcessImpl.java">the posix implementation of process
 * builder</a>, of which there are none known at this time.
 */
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
