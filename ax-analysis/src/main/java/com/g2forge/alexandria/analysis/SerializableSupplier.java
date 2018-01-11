package com.g2forge.alexandria.analysis;

import java.io.Serializable;
import java.util.function.Supplier;

@SuppressWarnings("hiding")
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {}