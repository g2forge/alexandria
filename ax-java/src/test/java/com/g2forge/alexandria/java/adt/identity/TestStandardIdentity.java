package com.g2forge.alexandria.java.adt.identity;

import org.junit.Assert;
import org.junit.Test;

public class TestStandardIdentity {
	@Test
	@SuppressWarnings("removal")
	public void equals() {
		Assert.assertTrue(IIdentity.standard().equals(new Integer(0), new Integer(0)));
	}

	@Test
	@SuppressWarnings("removal")
	public void hash() {
		Assert.assertEquals(IIdentity.standard().hashCode(new Integer(0)), IIdentity.standard().hashCode(new Integer(0)));
	}
}
