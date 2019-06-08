package com.g2forge.alexandria.java.core.helpers;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HTree {
	public static <N> Stream<N> dfs(N node, final Function<? super N, ? extends Collection<? extends N>> getChildren, boolean postorder) {
		final Collection<? extends N> children = getChildren.apply(node);
		if ((children == null) || children.isEmpty()) return Stream.of(node);
		else return children.stream().map(child -> dfs(child, getChildren, postorder)).reduce(Stream.of(node), postorder ? (s0, s1) -> Stream.concat(s1, s0) : Stream::concat);
	}

	public static <N> Optional<N> find(N node, final Function<? super N, ? extends Collection<? extends N>> getChildren, Predicate<? super N> find) {
		if (find.test(node)) return Optional.of(node);
		for (N child : getChildren.apply(node)) {
			final Optional<N> retVal = find(child, getChildren, find);
			if (retVal.isPresent()) return retVal;
		}
		return Optional.empty();
	}
}
