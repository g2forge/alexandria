package com.g2forge.alexandria.test;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsThrowable extends DiagnosingMatcher<Throwable> {
	@Factory
	public static <T extends Throwable> Matcher<Throwable> isThrowable(Class<T> type, Matcher<String> message) {
		return new IsThrowable(type, message);
	}

	@Factory
	public static <T extends Throwable> Matcher<Throwable> isThrowable(Class<T> type, String message) {
		return new IsThrowable(type, IsEqual.equalTo(message));
	}

	protected final Class<? extends Throwable> expectedClass;

	protected final Matcher<String> messageMatcher;

	@Override
	public void describeTo(Description description) {
		description.appendText("an instance of ").appendText(expectedClass.getName()).appendText(" with message ").appendDescriptionOf(messageMatcher);
	}

	@Override
	protected boolean matches(Object item, Description mismatch) {
		if (null == item) {
			mismatch.appendText("null");
			return false;
		}

		if (!expectedClass.isInstance(item)) {
			mismatch.appendValue(item).appendText(" is a " + item.getClass().getName());
			return false;
		}

		final String message = ((Throwable) item).getMessage();
		if (!messageMatcher.matches(message)) {
			messageMatcher.describeMismatch(message, mismatch);
			return false;
		}

		return true;
	}
}
