package com.g2forge.alexandria.adt.graph.v2.member;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

public abstract class AMember {
	protected abstract <V, E> IGraphGeneric<V, E> getGraphData(IGraphKey<V, E> key, boolean remove);

	protected abstract <V, E> void putGraphData(IGraphKey<V, E> key, IGraphGeneric<V, E> data);
}
