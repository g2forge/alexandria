package com.g2forge.alexandria.path.path;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

public class TestPath {
	@Test
	public void resolve0A() {
		final Path<String> a = new Path<>("a");
		HAssert.assertSame(a, Path.<String>createEmpty().resolve(a));
	}

	@Test
	public void resolveA0() {
		final Path<String> a = new Path<>("a");
		HAssert.assertSame(a, a.resolve(Path.createEmpty()));
	}

	@Test
	public void resolveAB() {
		HAssert.assertEquals(new Path<>("a", "b"), new Path<>("a").resolve(new Path<>("b")));
	}
}
