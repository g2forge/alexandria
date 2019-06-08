package com.g2forge.alexandria.adt.collection;

import com.g2forge.alexandria.java.close.ICloseable;

/**
 * A collection which can be closed when it is no longer needed. Can be used to represent a collection of objects from an I/O intensive backing store, for
 * example.
 *
 * @param <T> The type of the elements in this collection.
 */
public interface ICloseableCollection<T> extends ICollection<T>, ICloseable {}
