package com.g2forge.alexandria.adt.collection.collector;

import com.g2forge.alexandria.java.IFactory;

public interface ICollectionBuilder<C, T> extends ICollector<T>, IFactory<C> {}
