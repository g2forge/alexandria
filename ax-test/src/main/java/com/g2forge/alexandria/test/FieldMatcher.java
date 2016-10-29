package com.g2forge.alexandria.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.g2forge.alexandria.analysis.HAnalysis;
import com.g2forge.alexandria.analysis.SerializableFunction;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FieldMatcher<T> extends BaseMatcher<T> {
	@Data
	protected static class Field<T, F> {
		protected final SerializableFunction<T, F> field;

		protected final String path;

		protected void check(final T expected, final T actual, final Collection<Mismatch<?>> retVal) {
			final F expectedField = field.apply(expected);
			final F actualField = field.apply(actual);
			if (!FieldMatcher.equals(expectedField, actualField)) retVal.add(new Mismatch<>(path, expectedField, actualField));
		}
	}

	@Data
	public static class FieldSet<T> {
		protected final Collection<Field<T, ?>> fields;

		@SafeVarargs
		public FieldSet(SerializableFunction<T, ?>... fields) {
			this.fields = Stream.of(fields).map(f -> new Field<>(f, HAnalysis.getPath(f))).collect(Collectors.toList());
		}

		protected Collection<Mismatch<?>> check(T expected, T actual) {
			final Collection<Mismatch<?>> retVal = new ArrayList<>();
			for (Field<T, ?> field : fields) {
				field.check(expected, actual, retVal);
			}
			return retVal;
		}
	}

	@Data
	protected static class Mismatch<T> {
		protected final String name;

		protected final T expected;

		protected final T actual;
	}

	public static final String INDENT = "          ";

	public static boolean equals(Object o0, Object o1) {
		if (o0 == o1) return true;
		if ((o0 == null) || (o1 == null)) return false;

		if (o0 instanceof Object[] && o1 instanceof Object[]) return Arrays.equals((Object[]) o0, (Object[]) o1);
		return o0.equals(o1);
	}

	protected final T expected;

	protected final FieldSet<? super T> fields;

	@SafeVarargs
	public FieldMatcher(T expected, SerializableFunction<T, ?>... fields) {
		this.expected = expected;
		this.fields = new FieldSet<>(fields);
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		final Collection<Mismatch<?>> mismatches = mismatches(item);
		boolean first = true;
		for (Mismatch<?> mismatch : mismatches) {
			if (first) first = false;
			else {
				description.appendText("\n");
				description.appendText(INDENT);
			}
			description.appendText(mismatch.getName()).appendText(" was ").appendValue(mismatch.getActual()).appendText(" instead of ").appendValue(mismatch.getExpected());
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
	}

	@Override
	public boolean matches(Object item) {
		return mismatches(item).isEmpty();
	}

	protected Collection<Mismatch<?>> mismatches(Object item) {
		@SuppressWarnings("unchecked")
		final T actual = (T) item;
		return fields.check(expected, actual);
	}
}
