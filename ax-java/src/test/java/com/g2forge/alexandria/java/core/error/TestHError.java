package com.g2forge.alexandria.java.core.error;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestHError {
	@Test
	public void collectAllFail() {
		try {
			Stream.of(new OrThrowable<>(new Throwable("0")), new OrThrowable<>(new Throwable("1")), new OrThrowable<Integer>(2)).collect(HError.collector(() -> new RuntimeException("Parent"), false, Collectors.toList()));
			Assert.fail("Expected an exception!");
		} catch (RuntimeException exception) {
			Assert.assertEquals("Parent", exception.getMessage());
			Assert.assertEquals(2, exception.getSuppressed().length);
			Assert.assertEquals("0", exception.getSuppressed()[0].getMessage());
			Assert.assertEquals("1", exception.getSuppressed()[1].getMessage());
		}
	}

	@Test
	public void collectNoThrowables() {
		Stream.<Throwable>empty().collect(HError.collector(() -> new RuntimeException("Parent"), false));
	}

	@Test
	public void collectSomeSuccess() {
		final List<Integer> list = Stream.of(new OrThrowable<Integer>(new Throwable("0")), new OrThrowable<Integer>(1), new OrThrowable<Integer>(2)).collect(HError.collector(() -> new RuntimeException("Parent"), true, Collectors.toList()));
		Assert.assertEquals(HCollection.asList(1, 2), list);
	}

	@Test(expected = RuntimeException.class)
	public void collectThrowables() {
		Stream.of(new Throwable()).collect(HError.collector(() -> new RuntimeException("Parent"), false));
	}
}
