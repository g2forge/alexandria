package com.g2forge.alexandria.regex.regex;

import java.util.Set;

import org.junit.Test;

import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1G_O;
import com.g2forge.alexandria.java.core.helpers.HPrimitive;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.IPatternBuilder;
import com.g2forge.alexandria.regex.NamedCharacterClass;
import com.g2forge.alexandria.regex.regex.RegexPattern;
import com.g2forge.alexandria.test.HAssert;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestRegexPattern {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Coordinates {
		protected static TestRegexPattern.Coordinates parse(IMatch<TestRegexPattern.Coordinates> match) {
			final String group = match.getAsString(TestRegexPattern.Coordinates::getGroup);
			final String artifact = match.getAsString(TestRegexPattern.Coordinates::getArtifact);
			final TestRegexPattern.Version version = match.getAsObject(TestRegexPattern.Coordinates::getVersion);
			return new TestRegexPattern.Coordinates(group, artifact, version);
		}

		protected final String group;

		protected final String artifact;

		protected final Version version;
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Range {
		protected static TestRegexPattern.Range parse(IMatch<TestRegexPattern.Range> match) {
			final Version min = match.getAsObject(TestRegexPattern.Range::getMin);
			final Version max = match.getAsObject(TestRegexPattern.Range::getMax);
			return new TestRegexPattern.Range(min, max);
		}

		protected final Version min;

		protected final Version max;
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Version {
		public static Version parse(IMatch<Version> match) {
			final int major = Integer.parseInt(match.getAsString(TestRegexPattern.Version::getMajor));
			final Integer minor = HPrimitive.parseInteger(match.getAsString(TestRegexPattern.Version::getMinor));
			final Integer patch = HPrimitive.parseInteger(match.getAsString(TestRegexPattern.Version::getPatch));
			return new Version(major, minor, patch);
		}

		protected final int major;

		protected final Integer minor;

		protected final Integer patch;
	}

	@Getter(lazy = true)
	private static final RegexPattern<Coordinates> coordinates = computeCoordinates();

	@Getter(lazy = true)
	private static final RegexPattern<Range> range = computeRange();

	@Getter(lazy = true)
	private static final RegexPattern<Version> version = computeVersion();

	protected static RegexPattern<Coordinates> computeCoordinates() {
		final RegexPattern<Object> id = RegexPattern.builder().charClass(true).character(':').build().plus().build();
		final IPatternBuilder<Set<RegexPattern.Flag>, Coordinates, RegexPattern<?>, RegexPattern<Coordinates>> builder = RegexPattern.<Coordinates>builder();
		builder.group(Coordinates::getGroup, null).with(id).build().text(":").group(Coordinates::getArtifact, null).with(id).build().text(":");
		builder.group(Coordinates::getVersion, null).with(getVersion()).build();
		return builder.build(Coordinates::parse);
	}

	protected static RegexPattern<Range> computeRange() {
		return RegexPattern.<Range>builder().group(Range::getMin, null).with(getVersion()).build().text("-").group(Range::getMax, null).with(getVersion()).build().build(Range::parse);
	}

	protected static RegexPattern<Version> computeVersion() {
		final IPatternBuilder<Set<RegexPattern.Flag>, Version, RegexPattern<?>, RegexPattern<Version>> builder = RegexPattern.<Version>builder();
		builder.group(Version::getMajor, null).digit(10).plus().build();
		builder.group().text(".").group(Version::getMinor, null).digit(10).plus().build().group().text(".").group(Version::getPatch, null).digit(10).plus().build().build().opt().build().opt();
		return builder.build(TestRegexPattern.Version::parse);
	}

	@Test
	public void alt() {
		final RegexPattern<?> pattern = RegexPattern.builder().alt(RegexPattern.builder().text("a").build(), RegexPattern.builder().text("b").build()).build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("b").isEmpty());
	}

	@Test
	public void caseInsensitive() {
		final RegexPattern<Object> pattern = RegexPattern.builder(RegexPattern.Flag.CASE_INSENSITIVE).text("a").build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("A").isEmpty());
	}

	@Test
	public void characterClass() {
		final RegexPattern<?> charClass = RegexPattern.builder().charClass().range('0', '9').build().build();
		final RegexPattern<?> digit = RegexPattern.builder().digit(10).build();
		for (int i = 0; i < 10; i++) {
			final String string = Character.toString((char) ('0' + i));
			HAssert.assertFalse(string, charClass.match(string).isEmpty());
			HAssert.assertFalse(string, digit.match(string).isEmpty());
		}
	}

	@Test
	public void coordinates() {
		final Coordinates actual = getCoordinates().match("group:artifact:0.0.1").get();
		HAssert.assertEquals(new Coordinates("group", "artifact", new Version(0, 0, 1)), actual);
	}

	@Test
	public void groupNullArguments() {
		RegexPattern.builder().group();
	}

	@Test
	public void groupOptional() {
		final RegexPattern<?> pattern = RegexPattern.builder().text("a").group().text("b").build().opt().build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
	}

	@Test
	public void groupRequired() {
		final RegexPattern<?> pattern = RegexPattern.builder().text("a").group().text("b").build().build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
	}

	@Test
	public void match() {
		HAssert.assertFalse(RegexPattern.builder().text("a").build().match("a").isEmpty());
	}

	@Test
	public void named() {
		final RegexPattern<?> pattern = RegexPattern.builder().named(NamedCharacterClass.Space).build();
		HAssert.assertFalse(pattern.match(" ").isEmpty());
		HAssert.assertFalse(pattern.match("\t").isEmpty());
		HAssert.assertTrue(pattern.match("").isEmpty());
		HAssert.assertTrue(pattern.match("a").isEmpty());
	}

	@Test
	public void nonmatch() {
		HAssert.assertTrue(RegexPattern.builder().text("a").build().match("").isEmpty());
	}

	@Test
	public void plus() {
		final RegexPattern<?> pattern = RegexPattern.builder().text("a").text("b").plus().build();
		HAssert.assertTrue(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void star() {
		final RegexPattern<?> pattern = RegexPattern.builder().text("a").text("b").star().build();
		HAssert.assertFalse(pattern.match("a").isEmpty());
		HAssert.assertFalse(pattern.match("ab").isEmpty());
		HAssert.assertFalse(pattern.match("abb").isEmpty());
	}

	@Test
	public void versionMajor() {
		HAssert.assertEquals(new Version(0, null, null), getVersion().match("0").get());
	}

	@Test
	public void versionMinor() {
		HAssert.assertEquals(new Version(7, 200, null), getVersion().match("7.200").get());
	}

	@Test
	public void versionPatch() {
		HAssert.assertEquals(new Version(3, 1, 4), getVersion().match("3.1.4").get());
	}

	@Test
	public void with() {
		final RegexPattern<?> a = RegexPattern.builder().text("a").build();
		final RegexPattern<?> pattern = RegexPattern.builder().text("0").with(a).text("1").build();
		HAssert.assertFalse(pattern.match("0a1").isEmpty());
	}

	@Test
	public void withRange() {
		final Range actual = getRange().match("0.1.2-3.7").get();
		HAssert.assertEquals(new Range(new Version(0, 1, 2), new Version(3, 7, null)), actual);
	}

	@Test
	public void withVersion() {
		final RegexPattern<Version> pattern = RegexPattern.<Version>builder().with(getVersion()).build();
		HAssert.assertEquals(new Version(0, 1, null), pattern.match("0.1").get());
	}

	@Test
	public void withVersionTuple() {
		final RegexPattern<ITuple1G_<Version>> pattern = RegexPattern.<ITuple1G_<Version>>builder().group(ITuple1G_::get0, null).with(getVersion()).build().build(match -> new Tuple1G_O<>(match.getAsObject(ITuple1G_::get0)));
		HAssert.assertEquals(new Version(0, 1, null), pattern.match("0.1").get().get0());
	}
}
