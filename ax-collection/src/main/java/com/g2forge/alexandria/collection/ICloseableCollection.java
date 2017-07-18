package com.g2forge.alexandria.collection;

import com.g2forge.alexandria.java.close.ICloseable;

/**
 * A collection which can be closed when it is no longer needed. Can be used to represnt a collection of objects from an I/O intensive backing store, for example.
 * 
 * @author greg
 *
 * @param <T>
 */
public interface ICloseableCollection<T> extends ICollection<T>, ICloseable {}
