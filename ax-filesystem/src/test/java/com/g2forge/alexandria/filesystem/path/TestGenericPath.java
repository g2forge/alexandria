package com.g2forge.alexandria.filesystem.path;

import java.nio.file.Path;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.filesystem.path.GenericPath;

public class TestGenericPath extends ATestGeneric {
	@Test
	public void compareTo() {
		// TODO
	}

	@Test
	public void equalsAndHashcode() {
		final GenericPath left = create(null, "foo"), right = create(null, "foo");
		Assert.assertNotSame(left, right);
		Assert.assertTrue(left.equals(right));
		Assert.assertTrue(left.hashCode() == right.hashCode());
	}

	@Test
	public void iterator() {
		final Iterator<Path> iterator = parse("/a/b").iterator();
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(create(null, "a"), iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(create(null, "b"), iterator.next());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void names() {
		final GenericPath a = parse("a");
		Assert.assertEquals(1, a.getNameCount());
		Assert.assertEquals("a", a.getFileName().toString());
		Assert.assertEquals("a", a.getName(0).toString());
		Assert.assertEquals(a.getFileName(), a.getName(0));
	}

	@Test
	public void normalizeParent() {
		Assert.assertEquals(parse("foo"), parse("foo/bar/..").normalize());
	}

	@Test
	public void normalizeSelf() {
		Assert.assertEquals(create(null), parse(".").normalize());
	}

	@Test
	public void parent() {
		Assert.assertEquals(parse("/"), parse("/foo").getParent());
		Assert.assertEquals(parse("/foo"), parse("/foo/bar").getParent());
	}

	@Test
	public void relativize() {
		Assert.assertEquals(parse("bar"), parse("/foo/").relativize(parse("/foo/bar")));
		Assert.assertEquals(parse(".."), parse("/foo/bar").relativize(parse("/foo")));
		Assert.assertEquals(parse("."), parse("foo/bar").relativize(parse("foo/bar")));
		Assert.assertEquals(parse("../1"), parse("x/0").relativize(parse("x/1")));
	}

	@Test
	public void resolve() {
		Assert.assertEquals(parse("foo/bar"), parse("foo").resolve("bar"));
		Assert.assertEquals(parse("/foo/bar"), parse("/foo").resolve("bar"));
		Assert.assertEquals(parse("/bar"), parse("foo").resolve("/bar"));
	}

	@Test
	public void resolveSibling() {
		Assert.assertEquals(parse("a/c/../d/."), parse("a/b").resolveSibling("c/../d/."));
	}

	@Test
	public void root() {
		Assert.assertEquals(parse("/"), parse("/").getRoot());
		Assert.assertEquals(parse("/"), parse("/foo/bar").getRoot());
	}

	@Test
	public void subpath() {
		Assert.assertEquals(parse("b/c"), parse("/a/b/c").subpath(1, 3));
		Assert.assertEquals(parse("."), parse("/a").subpath(0, 0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void subpathBadIndices() {
		parse("a").subpath(1, 1);
	}

	@Test
	public void subpathSame() {
		final GenericPath path = parse("a/b");
		Assert.assertSame(path, path.subpath(0, 2));
	}

	@Test
	public void toAbsolutePath() {
		final GenericPath absolute = parse("/foo");
		Assert.assertEquals(absolute, absolute.toAbsolutePath());
		Assert.assertEquals(absolute, parse("foo").toAbsolutePath());
	}

	@Test
	public void toURI() {
		Assert.assertEquals("test:foo", parse("foo").toUri().toString());
	}
}
