package com.g2forge.alexandria.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;

public class TestRepeatableAnnotationMetadata {
	@Contained("A")
	public interface Annotated1 {}

	@Contained("A")
	@Contained("B")
	public interface Annotated2 {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Repeatable(Container.class)
	public @interface Contained {
		public String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Container {
		public Contained[] value();
	}

	@Test
	public void annotated1() {
		HAssert.assertEquals("A", IMetadata.of(Annotated1.class).getMetadata(Contained.class).value());
		HAssert.assertEquals(HCollection.asList("A"), Stream.of(IMetadata.of(Annotated1.class).getMetadata(Container.class).value()).map(Contained::value).collect(Collectors.toList()));
	}

	@Test
	public void annotated2() {
		HAssert.assertNull(IMetadata.of(Annotated2.class).getMetadata(Contained.class));
		HAssert.assertEquals(HCollection.asList("A", "B"), Stream.of(IMetadata.of(Annotated2.class).getMetadata(Container.class).value()).map(Contained::value).collect(Collectors.toList()));
	}
}
