package com.g2forge.alexandria.java.io.serdes;

import com.g2forge.alexandria.java.function.ICloseableConsumer1;
import com.g2forge.alexandria.java.io.dataaccess.IDataSink;

public interface ISerdesFactory_W<T> extends ISerdesFactory__<T> {
	public ICloseableConsumer1<? super T> create(IDataSink sink);
}
