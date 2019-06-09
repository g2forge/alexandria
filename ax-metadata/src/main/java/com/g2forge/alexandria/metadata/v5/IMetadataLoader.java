package com.g2forge.alexandria.metadata.v5;

@FunctionalInterface
public interface IMetadataLoader<T> {
	public T load(IElement element);
}
