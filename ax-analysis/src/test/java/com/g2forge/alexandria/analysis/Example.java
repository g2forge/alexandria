package com.g2forge.alexandria.analysis;

import lombok.Data;

public class Example {
	@Data
	public static class TestClass0 {
		protected TestClass1 foo;
	}

	@Data
	public static class TestClass1 {
		protected Object bar;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(AnalysisHelpers.getPath((SerializableFunction<TestClass0, ?>) (TestClass0 test) -> test.getFoo().getBar()));
	}
}
