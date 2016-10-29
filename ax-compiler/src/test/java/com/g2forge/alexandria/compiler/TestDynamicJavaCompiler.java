package com.g2forge.alexandria.compiler;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class TestDynamicJavaCompiler {
	public interface Interface {
		public String get();
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		final String string = "Hello";
		final Class<?> type = new DynamicJavaCompiler().compile("foo.bar.MyClass", "package foo.bar; public class MyClass implements " + Interface.class.getName().replace('$', '.') + " { @Override public String get() { return \"" + string + "\"; } }");
		Assert.assertEquals(string, ((Interface) type.newInstance()).get());
	}
}
