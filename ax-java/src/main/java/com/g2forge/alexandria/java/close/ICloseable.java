package com.g2forge.alexandria.java.close;

import java.io.Closeable;

@FunctionalInterface
public interface ICloseable extends Closeable {
	public void close();
}
