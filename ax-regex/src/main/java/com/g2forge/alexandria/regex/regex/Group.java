package com.g2forge.alexandria.regex.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_O;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.regex.IMatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
class Group<Result> {
	protected final LinkedHashSet<Integer> groupIndices;

	protected final Map<Object, Integer> fields = new HashMap<>();

	protected final List<Group<?>> children = new ArrayList<>();

	protected IFunction1<? super IMatch<Result>, Result> constructor = null;

	protected Group(int groupIndex) {
		this.groupIndices = new LinkedHashSet<>();
		groupIndices.add(groupIndex);
	}

	public void addChild(Group<?> child) {
		getChildren().add(child);
	}

	public IOptional<Result> construct(Matcher matcher) {
		final IFunction1<? super IMatch<Result>, Result> constructor = getConstructor();
		if (constructor == null) return NullableOptional.of(null);
		return NullableOptional.of(constructor.apply(new IMatch<Result>() {
			@Getter(lazy = true, value = AccessLevel.PROTECTED)
			private final Map<Integer, ? extends Group<?>> children = Group.this.getChildren().stream().flatMap(g -> g.getGroupIndices().stream().map(i -> new Tuple2G_O<Integer, Group<?>>(i, (Group<?>) g))).collect(HCollector.toMapTuples());

			@Override
			public <T> T getAsObject(ISerializableFunction1<? super Result, T> field) {
				final Integer groupIndex = toGroupIndex(field);
				final Group<?> child = getChildren().get(groupIndex);
				if (child == null) throw new IllegalArgumentException(String.format("Field %1$s was not recognized as a group with a constructor!", RegexPattern.getFieldID(field)));

				@SuppressWarnings("unchecked")
				final T cast = ((IOptional<T>) child.construct(matcher)).or(null);
				return cast;
			}

			@Override
			public String getAsString() {
				return matcher.group(HCollection.getFirst(getGroupIndices()));
			}

			@Override
			public String getAsString(ISerializableFunction1<? super Result, ?> field) {
				final Integer groupIndex = toGroupIndex(field);
				return matcher.group(groupIndex);
			}

			protected Integer toGroupIndex(ISerializableFunction1<? super Result, ?> field) {
				final Object id = RegexPattern.getFieldID(field);
				final Integer groupIndex = getFields().get(id);
				if (groupIndex == null) throw new IllegalArgumentException(String.format("Field %1$s was not recognized as a group!", id));
				return groupIndex;
			}
		}));
	}

	public Group<Result> copy(int groupIndexOffset) {
		final Group<Result> retVal = new Group<>(getGroupIndices().stream().map(i -> i + groupIndexOffset).collect(Collectors.toCollection(LinkedHashSet::new)));
		retVal.setConstructor(getConstructor());
		for (Map.Entry<Object, Integer> entry : getFields().entrySet()) {
			retVal.getFields().put(entry.getKey(), entry.getValue() + groupIndexOffset);
		}
		for (Group<?> child : getChildren()) {
			retVal.getChildren().add(child.copy(groupIndexOffset));
		}
		return retVal;
	}

	public <_Result> void setConstructor(IFunction1<? super IMatch<_Result>, _Result> constructor) {
		if (constructor == null) return;

		if ((getConstructor() != null) && !getConstructor().equals(constructor)) throw new IllegalArgumentException("Cannot change group constructor! You probably already told us how to parse the results when you created a pattern you used as an argument with \"with\".");
		@SuppressWarnings("unchecked")
		final IFunction1<? super IMatch<Result>, Result> cast = (IFunction1<? super IMatch<Result>, Result>) constructor;
		this.constructor = cast;
	}

	public Group<?> simplify() {
		final List<Group<?>> children = getChildren();
		final Map<Object, Integer> fields = getFields();

		if ((children.size() == 1) && fields.isEmpty() && (getConstructor() == null)) {
			final Group<?> retVal = HCollection.getOne(children).simplify();
			retVal.getGroupIndices().addAll(getGroupIndices());
			return retVal;
		}

		final ArrayList<Group<?>> copy = new ArrayList<>(children);
		children.clear();
		for (Group<?> child : copy) {
			final Group<?> simplified = child.simplify();
			if (simplified.getConstructor() != null) children.add(simplified);
			else {
				fields.putAll(simplified.getFields());
				children.addAll(simplified.getChildren());
			}
		}
		return this;
	}
}