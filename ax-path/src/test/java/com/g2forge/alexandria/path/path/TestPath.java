package com.g2forge.alexandria.path.path;

import org.junit.Test;

import com.g2forge.alexandria.java.adt.compare.ComparableComparator;
import com.g2forge.alexandria.java.core.error.IllegalOperationException;
import com.g2forge.alexandria.path.path.IPath.PathComparator;
import com.g2forge.alexandria.test.HAssert;

public class TestPath {
	@Test
	public void endsWithA() {
		HAssert.assertTrue(new Path<>("a").endsWith(new Path<>("a")));
	}

	@Test
	public void endsWithAB() {
		HAssert.assertTrue(new Path<>("a", "b").endsWith(new Path<>("b")));
	}

	@Test
	public void endsWithAC() {
		HAssert.assertFalse(new Path<>("a", "b").endsWith(new Path<>("a", "c")));
	}

	@Test
	public void endsWithAComponent() {
		HAssert.assertTrue(new Path<>("a").endsWith("a"));
	}

	@Test
	public void endsWithB() {
		HAssert.assertFalse(new Path<>("a").endsWith(new Path<>("b")));
	}

	@Test
	public void endsWithBComponent() {
		HAssert.assertFalse(new Path<>("a").endsWith("b"));
	}

	@Test
	public void endsWithCComponent() {
		HAssert.assertTrue(new Path<>("a", "b", "c").endsWith("c"));
	}

	@Test
	public void endsWithShort() {
		HAssert.assertFalse(new Path<>("a").endsWith(new Path<>("a", "b")));
	}

	@Test
	public void endsWithShortComponent() {
		HAssert.assertFalse(Path.createEmpty().endsWith("a"));
	}

	@Test
	public void getParent() {
		HAssert.assertEquals(new Path<>("a"), new Path<>("a", "b").getParent());
	}

	@Test(expected = IllegalOperationException.class)
	public void getParentIllegal() {
		Path.createEmpty().getParent();
	}

	@Test
	public void pathComparator_1() {
		final PathComparator<String, IPath<String>> pathComparator = new IPath.PathComparator<>(new ComparableComparator<>());
		HAssert.assertEquals(-1, pathComparator.compare(new Path<>("a"), new Path<>("a", "b")));
		HAssert.assertEquals(-1, pathComparator.compare(new Path<>("a"), new Path<>("b")));
	}

	@Test
	public void pathComparator0() {
		final PathComparator<String, IPath<String>> pathComparator = new IPath.PathComparator<>(new ComparableComparator<>());
		HAssert.assertEquals(0, pathComparator.compare(new Path<>("a"), new Path<>("a")));
		HAssert.assertEquals(0, pathComparator.compare(Path.createEmpty(), Path.createEmpty()));
	}

	@Test
	public void pathComparator1() {
		final PathComparator<String, IPath<String>> pathComparator = new IPath.PathComparator<>(new ComparableComparator<>());
		HAssert.assertEquals(1, pathComparator.compare(new Path<>("b"), new Path<>("a", "b")));
		HAssert.assertEquals(1, pathComparator.compare(new Path<>("a", "b"), new Path<>("a")));
	}

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

	@Test
	public void resolveSibling() {
		HAssert.assertEquals(new Path<>("a", "c"), new Path<>("a", "b").resolveSibling(new Path<>("c")));
	}

	@Test(expected = IllegalOperationException.class)
	public void resolveSiblingIllegal() {
		Path.createEmpty().resolveSibling(new Path<>("a"));
	}

	@Test
	public void size0() {
		HAssert.assertEquals(0, Path.createEmpty().size());
	}

	@Test
	public void size1() {
		HAssert.assertEquals(1, new Path<>("a").size());
	}

	@Test
	public void size2() {
		HAssert.assertEquals(2, new Path<>("a", "b").size());
	}

	@Test
	public void startsWithA() {
		HAssert.assertTrue(new Path<>("a").startsWith(new Path<>("a")));
	}

	@Test
	public void startsWithAB() {
		HAssert.assertTrue(new Path<>("a", "b").startsWith(new Path<>("a")));
	}

	@Test
	public void startsWithAC() {
		HAssert.assertFalse(new Path<>("a", "b").startsWith(new Path<>("a", "c")));
	}

	@Test
	public void startsWithAComponent() {
		HAssert.assertTrue(new Path<>("a").startsWith("a"));
	}

	@Test
	public void startsWithB() {
		HAssert.assertFalse(new Path<>("a").startsWith(new Path<>("b")));
	}

	@Test
	public void startsWithBComponent() {
		HAssert.assertFalse(new Path<>("a").startsWith("b"));
	}

	@Test
	public void startsWithCComponent() {
		HAssert.assertTrue(new Path<>("a", "b", "c").startsWith("a"));
	}

	@Test
	public void startsWithShort() {
		HAssert.assertFalse(new Path<>("a").startsWith(new Path<>("a", "b")));
	}

	@Test
	public void startsWithShortComponent() {
		HAssert.assertFalse(Path.createEmpty().startsWith("a"));
	}
}
