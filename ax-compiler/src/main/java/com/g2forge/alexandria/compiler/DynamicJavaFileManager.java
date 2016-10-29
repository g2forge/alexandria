package com.g2forge.alexandria.compiler;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public class DynamicJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
	protected final DynamicClassFile bytecode;

	protected final DynamicClassLoader classloader;

	protected DynamicJavaFileManager(JavaFileManager fileManager, DynamicClassFile bytecode, DynamicClassLoader classloader) {
		super(fileManager);
		this.bytecode = bytecode;
		this.classloader = classloader;
		this.classloader.add(bytecode);
	}

	@Override
	public ClassLoader getClassLoader(JavaFileManager.Location location) {
		return classloader;
	}

	@Override
	public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
		if (!name.equals(bytecode.getName())) throw new IllegalArgumentException();
		return bytecode;
	}
}