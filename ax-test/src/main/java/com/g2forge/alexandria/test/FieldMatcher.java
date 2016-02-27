package com.g2forge.alexandria.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.g2forge.alexandria.analysis.AnalysisHelpers;
import com.g2forge.alexandria.analysis.SerializableFunction;

import lombok.Data;

public class FieldMatcher<T> extends BaseMatcher<T> {
	@Data
	protected static class Mismatch<T> {
		protected final String name;

		protected final T expected;

		protected final T actual;
	}

	public static boolean equals(Object o0, Object o1) {
		if (o0 == o1) return true;
		if ((o0 == null) || (o1 == null)) return false;

		if (o0 instanceof Object[] && o1 instanceof Object[]) return Arrays.equals((Object[]) o0, (Object[]) o1);
		return o0.equals(o1);
	}

	protected final T expected;

	protected final SerializableFunction<T, ?>[] fields;

	@SafeVarargs
	public FieldMatcher(T expected, SerializableFunction<T, ?>... fields) {
		this.expected = expected;
		this.fields = fields;
	}

	protected <F> void check(final T actual, final Collection<Mismatch<?>> retVal, SerializableFunction<T, F> field) {
		final F expectedField = field.apply(expected);
		final F actualField = field.apply(actual);
		if (!equals(expectedField, actualField)) retVal.add(new Mismatch<>(AnalysisHelpers.getPath(field), expectedField, actualField));
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		final Collection<Mismatch<?>> mismatches = mismatches(item);
		boolean first = true;
		for (Mismatch<?> mismatch : mismatches) {
			if (first) first = false;
			else description.appendText("\n          ");
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

		final Collection<Mismatch<?>> retVal = new ArrayList<>();
		for (SerializableFunction<T, ?> field : fields) {
			check(actual, retVal, field);
		}
		return retVal;
	}
}
