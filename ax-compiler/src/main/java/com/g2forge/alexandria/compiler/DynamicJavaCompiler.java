package com.g2forge.alexandria.compiler;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.g2forge.alexandria.java.core.helpers.HString;

public class DynamicJavaCompiler {
	protected final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	public Class<?> compile(String name, String text) throws URISyntaxException, ClassNotFoundException {
		final DynamicClassLoader classloader = new DynamicClassLoader(getClass().getClassLoader());
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		final DynamicJavaFileManager fileManager = new DynamicJavaFileManager(javac.getStandardFileManager(diagnostics, null, null), new DynamicClassFile(name), classloader);

		final CompilationTask task = javac.getTask(null, fileManager, diagnostics, null, null, Arrays.asList(new DynamicSourceFile(name, text)));
		if (!task.call()) {
			final StringBuilder builder = new StringBuilder();
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				final String message = diagnostic.getMessage(Locale.ENGLISH);
				final String kind = HString.lowercase(diagnostic.getKind().toString());
				final JavaFileObject source = diagnostic.getSource();
				if (source != null) builder.append(String.format("%s on line %d in %s: %s%n", kind, diagnostic.getLineNumber(), source.toUri(), message));
				else builder.append(String.format("%s: %s%n", kind, message));

			}
			throw new RuntimeException(builder.toString());
		}

		return classloader.loadClass(name);
	}
}
