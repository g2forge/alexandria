package com.g2forge.alexandria.metadata.v4;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.metadata.v4.ViewAnnotatedElement;

public class TestView {
	public interface Parent {}

	public interface Child extends Parent {}

	public interface Unrelated {}

	@View(value = "parent", view = Parent.class)
	@View(value = "child", view = Child.class)
	public static class Foo {}

	@Test
	public void testChild() {
		test(Child.class, "parent", "child");
	}

	@Test
	public void testParent() {
		test(Parent.class, "parent");
	}

	@Test
	public void testUnrelated() {
		test(Unrelated.class);
	}
	
	protected static void test(Class<?> view, String...values) {
		final View[] annotations = new ViewAnnotatedElement(Foo.class, view).getAnnotationsByType(View.class);
		Assert.assertEquals(values.length, annotations.length);
		final String[] actuals = Stream.of(annotations).map(annotation -> annotation.value()).toArray(size -> new String[size]);
		Assert.assertArrayEquals(values, actuals);
	}
}
