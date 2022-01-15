package com.g2forge.alexandria.adt.graph.v2.member;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

public interface IGraphKey<V, E> extends IGraphGeneric<V, E> {
	public boolean isReadAllowed();
	
	public boolean isWriteAllowed();
}
