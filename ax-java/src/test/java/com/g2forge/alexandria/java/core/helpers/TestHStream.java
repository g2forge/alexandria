package com.g2forge.alexandria.java.core.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HStream;

public class TestHStream {
	@Test
	public void productMulti() {
		final List<Integer> actual = HStream.product((a, b) -> a | b, () -> Stream.of(0, 4), () -> Stream.of(0, 2), () -> Stream.of(0, 1)).collect(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(HCollection.asList(0, 1, 2, 3, 4, 5, 6, 7)), actual);
	}

	@Test
	public void productTyped() {
		final List<String> actual = HStream.product((String a, Integer b) -> a + b, () -> Stream.of("A", "B"), () -> Stream.of(0, 1)).collect(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(HCollection.asList("A0", "A1", "B0", "B1")), actual);
	}
}
