package com.g2forge.alexandria.java.core.resource;

import org.junit.Assert;
import org.junit.Test;

public class TestResource {
	@Test
	public void empty() {
		Assert.assertEquals("", HResource.read(getClass(), "empty.txt", true));
	}

	@Test
	public void line1() {
		Assert.assertEquals("A\n", HResource.read(getClass(), "line1.txt", true));
	}

	@Test
	public void nonewline() {
		Assert.assertEquals("A", HResource.read(getClass(), "nonewline.txt", true));
	}

	@Test
	public void line2() {
		Assert.assertEquals("A\nB\n", HResource.read(getClass(), "line2.txt", true));
	}

	@Test(expected = NullPointerException.class)
	public void missing() {
		HResource.read(getClass(), "missing.txt", true);
	}
}
