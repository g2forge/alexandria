package com.g2forge.alexandria.java.core.properties;

import org.junit.Assert;
import org.junit.Test;

public class TestIKnownProperty {
	public static enum KnownFlags implements IKnownPropertyBoolean {
		BASIC,
		OVERRIDDEN {
			@Override
			public void method() {}
		};

		public void method() {}
	}

	@Test
	public void basic() {
		Assert.assertEquals("knownflags.basic", KnownFlags.BASIC.getName());
	}

	@Test
	public void overridden() {
		Assert.assertEquals("knownflags.overridden", KnownFlags.OVERRIDDEN.getName());
	}
}
