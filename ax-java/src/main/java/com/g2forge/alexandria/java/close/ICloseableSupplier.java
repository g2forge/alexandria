package com.g2forge.alexandria.java.close;

import com.g2forge.alexandria.java.function.ISupplier;

public interface ICloseableSupplier<T> extends ICloseable, ISupplier<T> {}
