package com.g2forge.alexandria.java.io;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.IResource;
import com.g2forge.alexandria.java.core.resource.Resource;

public class TestHTextIO {
	@Test
	public void isEqual() {
		final IResource resource = new Resource(getClass(), "a.txt");
		Assert.assertTrue(HTextIO.isEqual(resource.getResourceAsStream(true), resource.getResourceAsStream(true)));
	}

	@Test
	public void notEqual() {
		Assert.assertFalse(HTextIO.isEqual(new Resource(getClass(), "a.txt").getResourceAsStream(true), new Resource(getClass(), "b.txt").getResourceAsStream(true)));
	}
}
