package com.g2forge.alexandria.regex.regex;

import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.IMatcher;
import com.g2forge.alexandria.regex.regex.RegexMatcher.Flag;

class State {
	protected final StringBuilder builder = new StringBuilder();

	protected int nGroups = 0;

	protected final Stack<Group<?, ?>> groups = new Stack<>();

	public State() {
		groups.push(new Group<>(0));
	}

	public State append(char text) {
		builder.append(text);
		return this;
	}

	public State append(String text) {
		builder.append(text);
		return this;
	}

	public <Parsed, Constructed> RegexMatcher<Constructed> build(Set<Flag> flags, IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>> constructor) {
		int flagsInt = 0;
		for (Flag flag : flags) {
			flagsInt |= flag.getFlag();
		}

		@SuppressWarnings("unchecked")
		final Group<Parsed, Constructed> cast = (Group<Parsed, Constructed>) group();
		cast.setConstructor(constructor);
		final Pattern pattern = Pattern.compile(builder.toString(), flagsInt);
		return new RegexMatcher<>(new Regex(pattern, nGroups, cast.simplify()));
	}

	public <Parsed, Constructed> State endGroup(IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends Constructed>> constructor) {
		final Group<?, ?> group = groups.pop();
		group.setConstructor(constructor);
		group().addChild(group);
		return append(')');
	}

	protected Group<?, ?> group() {
		return groups.peek();
	}

	public State startGroup(Object fieldID) {
		++nGroups;
		if (fieldID != null) group().getFields().put(fieldID, nGroups);
		groups.push(new Group<>(nGroups));
		return append('(');
	}

	public void with(IMatcher<?, Regex> matcher) {
		final Regex regex = matcher.getPattern();
		final Group<?, ?> theirGroup = regex.getGroup();

		final boolean wrap = (theirGroup.getConstructor() != null) && ((theirGroup.getChildren().size() != 1) || !HCollection.getOne(theirGroup.getChildren()).getGroupIndices().contains(1));
		if (wrap) startGroup(null);

		append(matcher.getPattern().getPattern().toString());
		final Group<?, ?> myGroup = group();
		myGroup.addChild(theirGroup.copy(nGroups));
		nGroups += matcher.getPattern().getNGroups();

		if (wrap) endGroup(null);
	}
}