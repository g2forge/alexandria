package com.g2forge.alexandria.generic.java.iterator;

import com.g2forge.alexandria.generic.java.tuple.ITuple1G_;

public interface IIterator<T> extends ITuple1G_<T> {
	public void commit();
	
	public IIterator<T> fork();
	
	/**
	 * @throws IllegalStateException if the iterator isn't at a valid position
	 */
	public T get0();
	
	public boolean isValid();
	
	public IIterator<T> next();
}
