package com.g2forge.alexandria.generic.java.iterator;

import java.util.ArrayList;
import java.util.List;

public class IteratorHelpers {
	public static <T> List<T> toList(IIterator<T> iterator) {
		final List<T> retVal = new ArrayList<>();
		for (; iterator.isValid(); iterator.next()) retVal.add(iterator.get0());
		return retVal;
	}
}
