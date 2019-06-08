package com.g2forge.alexandria.adt.collection;

import java.util.AbstractList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntegerRangeList extends AbstractList<Integer> {
	protected final int base;

	protected final int size;

	@Override
	public Integer get(int index) {
		return base + index;
	}

	@Override
	public int size() {
		return size;
	}
}
