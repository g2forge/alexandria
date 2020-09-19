package com.g2forge.alexandria.parse.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.parse.IMatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
@ToString
class Group<Parsed, Constructed> {
	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	protected class Match implements IMatch<Parsed> {
		protected final Matcher matcher;

		@Override
		public <T> T getAsObject(ISerializableFunction1<? super Parsed, T> field) {
			final Integer groupIndex = toGroupIndex(field);
			return getAsObject(groupIndex, RegexMatcher.getFieldID(field));
		}

		protected <T> T getAsObject(final Integer groupIndex, Object fieldID) {
			final List<Group<?, ?>> children = getIndexed().get(groupIndex);
			if (children == null) { throw new IllegalArgumentException(String.format("Field %1$s was not recognized as a group with a constructor!", fieldID)); }

			final List<?> list = children.stream().map(child -> child.construct(matcher)).filter(o -> !o.isEmpty()).map(IOptional::get).collect(Collectors.toList());
			@SuppressWarnings("unchecked")
			final T cast = (T) HCollection.getOne(list);
			return cast;
		}

		@Override
		public String getAsString() {
			return matcher.group(HCollection.getFirst(getGroupIndices()));
		}

		@Override
		public String getAsString(ISerializableFunction1<? super Parsed, ?> field) {
			final Integer groupIndex = toGroupIndex(field);
			return matcher.group(groupIndex);
		}

		protected Integer toGroupIndex(ISerializableFunction1<? super Parsed, ?> field) {
			final Object id = RegexMatcher.getFieldID(field);
			final Integer groupIndex = getFields().get(id);
			if (groupIndex == null) { throw new IllegalArgumentException(String.format("Field %1$s was not recognized as a group!", id)); }
			return groupIndex;
		}
	}

	protected final LinkedHashSet<Integer> groupIndices;

	protected final Map<Object, Integer> fields = new HashMap<>();

	protected final List<Group<?, ?>> children = new ArrayList<>();

	protected IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>> constructor = null;

	@ToString.Exclude
	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final Map<Integer, List<Group<?, ?>>> indexed = Group.this.getChildren().stream().collect(HCollector.multiGroupingBy(Group::getGroupIndices));

	protected Group(int groupIndex) {
		this.groupIndices = new LinkedHashSet<>();
		groupIndices.add(groupIndex);
	}

	public void addChild(Group<?, ?> child) {
		getChildren().add(child);
	}

	public IOptional<? extends Constructed> construct(Matcher matcher) {
		final IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>> constructor = getConstructor();
		final Match match = new Match(matcher);
		if (constructor == null) {
			// Maybe this is the top level group for a single value
			final Set<Integer> groupIndices = getGroupIndices();
			if (groupIndices.contains(1) && groupIndices.contains(0)) {
				try {
					return NullableOptional.of(match.getAsObject(1, 1));
				} catch (IllegalArgumentException e) {}
			}
			return NullableOptional.of(null);
		}
		return constructor.apply(match);
	}

	public Group<Parsed, Constructed> copy(int groupIndexOffset) {
		final Group<Parsed, Constructed> retVal = new Group<>(getGroupIndices().stream().map(i -> i + groupIndexOffset).collect(Collectors.toCollection(LinkedHashSet::new)));
		retVal.setConstructor(getConstructor());
		for (Map.Entry<Object, Integer> entry : getFields().entrySet()) {
			retVal.getFields().put(entry.getKey(), entry.getValue() + groupIndexOffset);
		}
		for (Group<?, ?> child : getChildren()) {
			retVal.getChildren().add(child.copy(groupIndexOffset));
		}
		return retVal;
	}

	public <_Parsed, _Constructed> void setConstructor(IFunction1<? super IMatch<_Parsed>, ? extends IOptional<? extends _Constructed>> constructor) {
		if (constructor == null) return;

		if ((getConstructor() != null) && !getConstructor().equals(constructor)) throw new IllegalArgumentException("Cannot change group constructor! You probably already told us how to parse the results when you created a pattern you used as an argument with \"with\".");
		@SuppressWarnings("unchecked")
		final IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>> cast = (IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>>) constructor;
		this.constructor = cast;
	}

	public Group<?, ?> simplify() {
		final List<Group<?, ?>> children = getChildren();
		final Map<Object, Integer> fields = getFields();

		final boolean hasNoConstructor = getConstructor() == null;
		if ((children.size() == 1) && fields.isEmpty() && hasNoConstructor) {
			final Group<?, ?> retVal = HCollection.getOne(children).simplify();
			retVal.getGroupIndices().addAll(getGroupIndices());
			return retVal;
		}

		final ArrayList<Group<?, ?>> copy = new ArrayList<>(children);
		children.clear();
		for (Group<?, ?> child : copy) {
			final Group<?, ?> simplified = child.simplify();
			if (simplified.getConstructor() != null) {
				children.add(simplified);
				if (hasNoConstructor) simplified.getGroupIndices().addAll(getGroupIndices());
			} else {
				fields.putAll(simplified.getFields());
				children.addAll(simplified.getChildren());
			}
		}
		return this;
	}
}