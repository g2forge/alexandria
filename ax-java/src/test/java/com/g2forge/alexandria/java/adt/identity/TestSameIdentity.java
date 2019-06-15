package com.g2forge.alexandria.java.adt.identity;

import org.junit.Assert;
import org.junit.Test;

public class TestSameIdentity {
	@Test
	public void equals() {
		Assert.assertFalse(IIdentity.same().equals(new Integer(0), new Integer(0)));
	}

	@Test
	public void hash() {
		Assert.assertNotEquals(IIdentity.same().hashCode(new Integer(0)), IIdentity.standard().hashCode(new Integer(0)));
	}
}
