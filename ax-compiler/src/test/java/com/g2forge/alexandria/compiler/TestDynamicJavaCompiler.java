package com.g2forge.alexandria.compiler;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.compiler.diagnostic.DataDiagnostic;
import com.g2forge.alexandria.compiler.diagnostic.DiagnosticMatcher;
import com.g2forge.alexandria.test.HAssert;

public class TestDynamicJavaCompiler {
	public interface Invocable {
		public String get();
	}

	public interface Typed<T extends Invocable> {}

	public static String toString(Class<?> type) {
		return type.getName().replace('$', '.');
	}

	@Test
	public void error() throws ClassNotFoundException, URISyntaxException {
		final String text = "package foo.bar; public class MyClass { public void method(" + toString(Typed.class) + "<String> foo) { } }";
		try {
			new DynamicJavaCompiler().compile("foo.bar.MyClass", text);
		} catch (DynamicJavaCompilerException exception) {
			final List<? extends Diagnostic<? extends JavaFileObject>> diagnostics = exception.getDiagnostics().stream().filter(d -> !d.getCode().startsWith("compiler.warn.proc.")).collect(Collectors.toList());
			HAssert.assertThat(diagnostics, Matchers.hasItem(new DiagnosticMatcher<>(new DataDiagnostic<>(Diagnostic.Kind.ERROR, null, 1l, text.indexOf("String"), "compiler.err.not.within.bounds", null))));
		}
	}

	@Test
	public void invoke() throws InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final String string = "Hello";
		final Class<?> type = new DynamicJavaCompiler().compile("foo.bar.MyClass", "package foo.bar; public class MyClass implements " + toString(Invocable.class) + " { @Override public String get() { return \"" + string + "\"; } }");
		Assert.assertEquals(string, ((Invocable) type.getDeclaredConstructor().newInstance()).get());
	}
}
