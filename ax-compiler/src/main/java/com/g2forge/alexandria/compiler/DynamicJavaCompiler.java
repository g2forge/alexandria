package com.g2forge.alexandria.compiler;

import java.net.URISyntaxException;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class DynamicJavaCompiler {
	protected final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	public Class<?> compile(String name, String text) throws URISyntaxException, ClassNotFoundException {
		final DynamicClassLoader classloader = new DynamicClassLoader(getClass().getClassLoader());
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		final DynamicJavaFileManager fileManager = new DynamicJavaFileManager(javac.getStandardFileManager(diagnostics, null, null), new DynamicClassFile(name), classloader);

		final CompilationTask task = javac.getTask(null, fileManager, diagnostics, null, null, Arrays.asList(new DynamicSourceFile(name, text)));
		if (!task.call()) throw new DynamicJavaCompilerException(diagnostics.getDiagnostics());

		return classloader.loadClass(name);
	}
}
