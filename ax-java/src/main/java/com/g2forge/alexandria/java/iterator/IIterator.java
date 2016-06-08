package com.g2forge.alexandria.java.iterator;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.tuple.ITuple1G_;

public interface IIterator<T> extends ITuple1G_<T> {
	public default List<T> toList(IIterator<T> iterator) {
		final List<T> retVal = new ArrayList<>();
		for (; iterator.isValid(); iterator.next())
			retVal.add(iterator.get0());
		return retVal;
	}

	public void commit();

	public IIterator<T> fork();

	/**
	 * @throws IllegalStateException
	 *             if the iterator isn't at a valid position
	 */
	public T get0();

	public boolean isValid();

	public IIterator<T> next();
}
