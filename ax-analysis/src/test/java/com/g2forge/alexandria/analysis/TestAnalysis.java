package com.g2forge.alexandria.analysis;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;

public class TestAnalysis {
	@Data
	public static class TestClass0 {
		protected TestClass1 foo;
	}

	@Data
	public static class TestClass1 {
		protected Object bar;
	}

	@Test
	public void test() throws ClassNotFoundException, Error {
		Assert.assertEquals("foo.bar", HAnalysis.getPath((TestClass0 test) -> test.getFoo().getBar()));
	}
}
