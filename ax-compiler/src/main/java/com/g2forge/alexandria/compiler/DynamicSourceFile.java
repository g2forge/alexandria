package com.g2forge.alexandria.compiler;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class DynamicSourceFile extends SimpleJavaFileObject {
	protected static final String STRING = "string";

	protected final String text;

	public DynamicSourceFile(String name, String text) {
		super(URI.create(STRING + ":///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.text = text;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return text;
	}
}
