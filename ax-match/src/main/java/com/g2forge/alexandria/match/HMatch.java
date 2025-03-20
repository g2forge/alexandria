package com.g2forge.alexandria.match;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IPredicate1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMatch {
	public static IPredicate1<String> createPredicate(final String matchDescription) {
		if (matchDescription.startsWith(RegexMatch.PREFIX_REGEX)) return new RegexMatch(matchDescription);
		if (matchDescription.startsWith(GlobMatch.PREFIX_GLOB)) return new GlobMatch(matchDescription);
		return new LiteralMatch(matchDescription);
	}

	public static IPredicate1<Object> createPredicate(boolean any, String... matchDescriptions) {
		return createPredicate(any, Object::toString, HCollection.asList(matchDescriptions));
	}

	public static IPredicate1<Object> createPredicate(boolean any, final Collection<? extends String> matchDescriptions) {
		return createPredicate(any, Object::toString, matchDescriptions);
	}

	public static <T> IPredicate1<T> createPredicate(boolean any, IFunction1<? super T, ? extends String> function, String... matchDescriptions) {
		return createPredicate(any, function, HCollection.asList(matchDescriptions));
	}

	public static <T> IPredicate1<T> createPredicate(boolean any, IFunction1<? super T, ? extends String> function, Collection<? extends String> matchDescriptions) {
		if ((matchDescriptions == null) || matchDescriptions.isEmpty()) return IPredicate1.create(false);
		final List<IPredicate1<String>> predicates = matchDescriptions.stream().map(HMatch::createPredicate).collect(Collectors.toList());
		if (any) return value -> {
			final String string = function.apply(value);
			for (IPredicate1<String> predicate : predicates) {
				if (predicate.test(string)) return true;
			}
			return false;
		};
		else return value -> {
			final String string = function.apply(value);
			for (IPredicate1<String> predicate : predicates) {
				if (!predicate.test(string)) return false;
			}
			return true;
		};
	}
}
