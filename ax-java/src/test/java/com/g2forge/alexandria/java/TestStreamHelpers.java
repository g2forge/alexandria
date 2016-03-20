package com.g2forge.alexandria.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class TestStreamHelpers {
	@Test
	public void productMulti() {
		final List<Integer> actual = StreamHelpers.product((a, b) -> a | b, () -> Stream.of(0, 4), () -> Stream.of(0, 2), () -> Stream.of(0, 1)).collect(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)), actual);
	}

	@Test
	public void productTyped() {
		final List<String> actual = StreamHelpers.product((String a, Integer b) -> a + b, () -> Stream.of("A", "B"), () -> Stream.of(0, 1)).collect(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(Arrays.asList("A0", "A1", "B0", "B1")), actual);
	}
}
