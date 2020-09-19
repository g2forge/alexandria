package com.g2forge.alexandria.regex.regex;

import org.junit.Test;

import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1G_O;
import com.g2forge.alexandria.java.core.helpers.HPrimitive;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.IMatcher;
import com.g2forge.alexandria.regex.IMatcherBuilder;
import com.g2forge.alexandria.test.HAssert;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestCompositeRegexMatcher {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Coordinates {
		protected static Coordinates parse(IMatch<Coordinates> match) {
			final String group = match.getAsString(Coordinates::getGroup);
			final String artifact = match.getAsString(Coordinates::getArtifact);
			final Version version = match.getAsObject(Coordinates::getVersion);
			return new Coordinates(group, artifact, version);
		}

		protected final String group;

		protected final String artifact;

		protected final Version version;
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Range {
		protected static Range parse(IMatch<Range> match) {
			final Version min = match.getAsObject(Range::getMin);
			final Version max = match.getAsObject(Range::getMax);
			return new Range(min, max);
		}

		protected final Version min;

		protected final Version max;
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Version {
		public static Version parse(IMatch<Version> match) {
			final int major = Integer.parseInt(match.getAsString(Version::getMajor));
			final Integer minor = HPrimitive.parseInteger(match.getAsString(Version::getMinor));
			final Integer patch = HPrimitive.parseInteger(match.getAsString(Version::getPatch));
			return new Version(major, minor, patch);
		}

		protected final int major;

		protected final Integer minor;

		protected final Integer patch;
	}

	@Getter(lazy = true)
	private static final IMatcher<Coordinates, Regex> coordinates = computeCoordinates();

	@Getter(lazy = true)
	private static final IMatcher<Range, Regex> range = computeRange();

	@Getter(lazy = true)
	private static final IMatcher<Version, Regex> version = computeVersion();

	protected static IMatcher<Coordinates, Regex> computeCoordinates() {
		final IMatcher<String, Regex> id = RegexMatcher.builder().charClass(true, cc -> cc.character(':')).plus().build();
		final IMatcherBuilder<Coordinates, Regex> builder = RegexMatcher.builder();
		builder.with(Coordinates::getGroup, id).text(":").with(Coordinates::getArtifact, id).text(":");
		builder.with(Coordinates::getVersion, getVersion()).build();
		return builder.buildReq(Coordinates::parse);
	}

	protected static IMatcher<Range, Regex> computeRange() {
		return RegexMatcher.<Range>builder().with(Range::getMin, getVersion()).text("-").with(Range::getMax, getVersion()).buildReq(Range::parse);
	}

	protected static IMatcher<Version, Regex> computeVersion() {
		final IMatcherBuilder<Version, Regex> builder = RegexMatcher.<Version>builder();
		builder.group(Version::getMajor, g -> g.digit(10).plus());
		builder.group(g0 -> g0.text(".").group(Version::getMinor, g -> g.digit(10).plus()).group(g1 -> g1.text(".").group(Version::getPatch, g2 -> g2.digit(10).plus())).opt()).opt();
		return builder.buildReq(Version::parse);
	}

	@Test
	public void coordinates() {
		final Coordinates actual = getCoordinates().match("group:artifact:0.0.1").get();
		HAssert.assertEquals(new Coordinates("group", "artifact", new Version(0, 0, 1)), actual);
	}

	@Test
	public void topGroup() {
		final IMatcher<Integer, Regex> integer = RegexMatcher.<Integer>builder().digit(10).plus().buildString(Integer::parseInt);
		final IMatcher<Version, Regex> version = RegexMatcher.<Version>builder().with(Version::getMajor, integer).buildReq(match -> new Version(match.getAsObject(Version::getMajor), null, null));
		HAssert.assertEquals(new Version(15, null, null), version.match("15").get());
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
	public void withRange() {
		final Range actual = getRange().match("0.1.2-3.7").get();
		HAssert.assertEquals(new Range(new Version(0, 1, 2), new Version(3, 7, null)), actual);
	}

	@Test
	public void withVersion() {
		final IMatcher<Version, Regex> pattern = RegexMatcher.<Version>builder().with(getVersion()).build();
		HAssert.assertEquals(new Version(0, 1, null), pattern.match("0.1").get());
	}

	@Test
	public void withVersionTuple() {
		final IMatcher<ITuple1G_<Version>, Regex> pattern = RegexMatcher.<ITuple1G_<Version>>builder().with(ITuple1G_::get0, getVersion()).buildReq(match -> new Tuple1G_O<>(match.getAsObject(ITuple1G_::get0)));
		HAssert.assertEquals(new Version(0, 1, null), pattern.match("0.1").get().get0());
	}
}
