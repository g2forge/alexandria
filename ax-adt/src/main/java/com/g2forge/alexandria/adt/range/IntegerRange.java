package com.g2forge.alexandria.adt.range;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class IntegerRange implements IRange<Integer> {
	protected final int min;

	protected final int max;

	public IntegerRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Integer getMax() {
		return max;
	}

	@Override
	public Integer getMin() {
		return min;
	}

	public IntegerRange wrapSubRange(IRange<Integer> subrange) {
		final int delta = getMax() - getMin() + 1;
		final int min = subrange.getMin() < 0 ? subrange.getMin() + delta : subrange.getMin();
		final int max = subrange.getMax() < 0 ? subrange.getMax() + delta : subrange.getMax();
		final IntegerRange retVal = new IntegerRange(min, max);
		validateSubRange(retVal);
		return retVal;
	}
}
