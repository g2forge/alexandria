package com.g2forge.alexandria.regex.regex;

import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.regex.RegexPattern.Flag;

class State {
	protected final StringBuilder builder = new StringBuilder();

	protected int nGroups = 0;

	protected final Stack<Group<?>> groups = new Stack<>();

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

	public <Result> RegexPattern<Result> build(Set<Flag> flags, IFunction1<? super IMatch<Result>, Result> constructor) {
		int flagsInt = 0;
		for (Flag flag : flags) {
			flagsInt |= flag.getFlag();
		}

		@SuppressWarnings("unchecked")
		final Group<Result> cast = (Group<Result>) group();
		cast.setConstructor(constructor);
		final Pattern pattern = Pattern.compile(builder.toString(), flagsInt);
		return new RegexPattern<>(pattern, nGroups, cast.simplify());
	}

	public <Result> State endGroup(IFunction1<? super IMatch<Result>, Result> constructor) {
		final Group<?> group = groups.pop();
		group.setConstructor(constructor);
		group().addChild(group);
		return append(')');
	}

	protected Group<?> group() {
		return groups.peek();
	}

	public State startGroup(Object fieldID) {
		++nGroups;
		if (fieldID != null) group().getFields().put(fieldID, nGroups);
		groups.push(new Group<>(nGroups));
		return append('(');
	}

	public void with(RegexPattern<?> pattern) {
		append(pattern.getPattern().toString());
		final Group<?> group = group();
		group.addChild(pattern.getGroup().copy(nGroups));
		nGroups += pattern.getNGroups();
	}
}