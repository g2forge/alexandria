package com.g2forge.alexandria.java.project;

import org.junit.Assert;
import org.junit.Test;

public class TestHProject {
	@Test
	public void hasLocation() {
		final Location location = HProject.getLocation(getClass());
		Assert.assertNotNull(location);
		Assert.assertNotNull(location.getLayout());
		Assert.assertNotNull(location.getProject().getRoot());
		Assert.assertNotNull(location.getTarget());
	}
}
