package com.g2forge.alexandria.java.adt.identity;

import org.junit.Assert;
import org.junit.Test;

public class TestStandardIdentity {
	@Test
	public void equals() {
		Assert.assertTrue(IIdentity.standard().equals(Integer.valueOf(0), Integer.valueOf(0)));
	}

	@Test
	public void hash() {
		Assert.assertEquals(IIdentity.standard().hashCode(Integer.valueOf(0)), IIdentity.standard().hashCode(Integer.valueOf(0)));
	}
}
