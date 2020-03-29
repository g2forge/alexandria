package com.g2forge.alexandria.java.text.escape;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PROTECTED)
public class SequenceEscaper extends AEscaper {
	@Accessors(fluent = true, chain = true)
	public static class SequenceEscaperBuilder implements IBuilder<SequenceEscaper> {
		@Setter
		protected String prefix;

		@Setter
		protected String postfix;

		protected final Map<String, String> escape = new HashMap<>();

		protected final Map<String, String> unescape = new HashMap<>();

		@Override
		public SequenceEscaper build() {
			return new SequenceEscaper(prefix, postfix, escape, unescape);
		}

		public SequenceEscaperBuilder escape(String unescaped, String escaped, boolean required) {
			if (escape.containsKey(unescaped) || unescape.containsKey(escaped)) throw new IllegalStateException();

			if (required) escape.put(unescaped, escaped);
			unescape.put(escaped, unescaped);
			return self();
		}

		protected SequenceEscaperBuilder self() {
			return this;
		}
	}

	public static SequenceEscaperBuilder builder() {
		return new SequenceEscaperBuilder();
	}

	protected final Map<Character, Map<String, String>> escape;

	protected final Map<Character, Map<String, String>> unescape;

	protected SequenceEscaper(String prefix, String postfix, Map<String, String> escape, Map<String, String> unescape) {
		super(prefix, postfix);
		this.escape = escape.entrySet().stream().collect(Collectors.groupingBy(e -> e.getKey().charAt(0), HCollector.toMapEntries()));
		this.unescape = unescape.entrySet().stream().collect(Collectors.groupingBy(e -> e.getKey().charAt(0), HCollector.toMapEntries()));
	}

	protected Change computeChage(final Map<Character, Map<String, String>> characterMap, String string, int index) {
		final Map<String, String> map = characterMap.get(string.charAt(index));
		if (map != null) for (Map.Entry<String, String> entry : map.entrySet()) {
			if (string.startsWith(entry.getKey(), index)) return new Change(entry.getKey().length(), s -> entry.getValue());
		}
		return null;
	}

	@Override
	protected Change computeEscape(String string, int index) {
		return computeChage(getEscape(), string, index);
	}

	@Override
	protected Pattern computeRequiresEscapePattern() {
		return Pattern.compile(getEscape().values().stream().flatMap(map -> map.keySet().stream()).map(Pattern::quote).collect(Collectors.joining("|")));
	}

	@Override
	protected Change computeUnescape(String string, int index) {
		return computeChage(getUnescape(), string, index);
	}
}