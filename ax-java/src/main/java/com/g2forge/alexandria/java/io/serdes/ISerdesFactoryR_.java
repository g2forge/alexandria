package com.g2forge.alexandria.java.io.serdes;

import com.g2forge.alexandria.java.close.ICloseableSupplier;
import com.g2forge.alexandria.java.io.dataaccess.IDataSource;

public interface ISerdesFactoryR_<T> extends ISerdesFactory__<T> {
	public ICloseableSupplier<T> create(IDataSource source);
}
