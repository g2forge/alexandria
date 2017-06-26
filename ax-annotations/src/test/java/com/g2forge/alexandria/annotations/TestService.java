package com.g2forge.alexandria.annotations;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

@Service(ITestService.class)
public class TestService implements ITestService {
	protected final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	public static void main(String[] args) throws IOException {
		new TestService().execute();
	}

	public void execute() throws IOException {
		final Iterable<JavaFileObject> files = getSourceFiles("src/test/java");

		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		final JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(System.out), fileManager, null, null, null, files);
		task.setProcessors(Arrays.asList(new AnnotationProcessor()));
		task.call();
	}

	public Iterable<JavaFileObject> getSourceFiles(String path) throws IOException {
		final StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);
		files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(path)));
		return files.list(StandardLocation.SOURCE_PATH, "", Collections.singleton(JavaFileObject.Kind.SOURCE), true);
	}
}
