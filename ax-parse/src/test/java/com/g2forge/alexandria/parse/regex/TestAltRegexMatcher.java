package com.g2forge.alexandria.parse.regex;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.test.HAssert;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestAltRegexMatcher {
	public enum Enum {
		A,
		B;
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Record {
		protected final Enum value;
	}

	@Getter(lazy = true)
	private static final IMatcher<Enum, Regex> matcher = RegexMatcher.builder().alt(Stream.of(Enum.values()).map(e -> RegexMatcher.builder().text(e.name()).buildFlag(e)).collect(Collectors.toList())).build();

	@Test
	public void nested() {
		final IMatcher<Record, Regex> matcher = RegexMatcher.<Record>builder().with(Record::getValue, getMatcher()).buildReq(match -> new Record(match.getAsObject(Record::getValue)));
		HAssert.assertEquals(Enum.B, matcher.match("B").get().getValue());
	}

	@Test
	public void top() {
		final IOptional<Enum> match = getMatcher().match("A");
		HAssert.assertEquals(Enum.A, match.get());
	}
}
