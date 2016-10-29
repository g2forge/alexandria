package com.g2forge.alexandria.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

public class DynamicClassFile extends SimpleJavaFileObject {
	protected final ByteArrayOutputStream stream = new ByteArrayOutputStream();

	public DynamicClassFile(String name) throws URISyntaxException {
		super(new URI(name), Kind.CLASS);
	}

	protected byte[] getByteArray() {
		return stream.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return stream;
	}
}
