package com.g2forge.alexandria.adt.graph.v2.member;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

public class ASingleGraphMember extends AMember {
	private IGraphKey<?, ?> graphKey;

	private IGraphGeneric<?, ?> graphData;

	@Override
	protected <V, E> IGraphGeneric<V, E> getGraphData(IGraphKey<V, E> key, boolean remove) {
		if (!key.isReadAllowed()) throw new IllegalStateException();
		if ((graphKey == null) || (graphKey != key)) throw new IllegalArgumentException("No data for this graph key");
		@SuppressWarnings("unchecked")
		final IGraphGeneric<V, E> retVal = (IGraphGeneric<V, E>) graphData;
		if (remove) {
			graphKey = null;
			graphData = null;
		}
		return retVal;
	}

	@Override
	protected <V, E> void putGraphData(IGraphKey<V, E> key, IGraphGeneric<V, E> data) {
		if (!key.isWriteAllowed()) throw new IllegalStateException();
		if ((graphKey != null) && (graphKey != key)) throw new IllegalArgumentException("Cannot add this member to more than one graph");
		graphKey = key;
		graphData = data;
	}
}
