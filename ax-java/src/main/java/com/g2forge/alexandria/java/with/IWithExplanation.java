package com.g2forge.alexandria.java.with;

import com.g2forge.alexandria.java.adt.attributes.IHasExplanation;
import com.g2forge.alexandria.java.function.ISupplier;

public interface IWithExplanation<T, E> extends ISupplier<T>, IHasExplanation<E> {}
