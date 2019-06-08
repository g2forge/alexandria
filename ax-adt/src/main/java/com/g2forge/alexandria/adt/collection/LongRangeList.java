package com.g2forge.alexandria.adt.collection;

import java.util.AbstractList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LongRangeList extends AbstractList<Long> {
	protected final long base;

	protected final int size;

	@Override
	public Long get(int index) {
		return base + index;
	}

	@Override
	public int size() {
		return size;
	}
}
