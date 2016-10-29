package com.g2forge.alexandria.compiler;

import java.util.HashMap;
import java.util.Map;

public class DynamicClassLoader extends ClassLoader {
	protected final Map<String, DynamicClassFile> classes = new HashMap<>();

	public DynamicClassLoader() {}

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}

	protected void add(DynamicClassFile bytecode) {
		classes.put(bytecode.getName(), bytecode);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (classes.containsKey(name)) {
			final byte[] array = classes.get(name).getByteArray();
			return defineClass(name, array, 0, array.length);
		}
		return super.findClass(name);
	}
}
