package com.g2forge.alexandria.adt.graph.v2.member;

import java.util.Map;
import java.util.WeakHashMap;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

import lombok.AccessLevel;
import lombok.Getter;

public class AMultiGraphMember extends AMember {
	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final Map<IGraphKey<?, ?>, IGraphGeneric<?, ?>> graphData = new WeakHashMap<>();

	@Override
	protected <V, E> IGraphGeneric<V, E> getGraphData(IGraphKey<V, E> key, boolean remove) {
		if (!key.isReadAllowed()) throw new IllegalStateException();
		final Map<IGraphKey<?, ?>, IGraphGeneric<?, ?>> graphData = getGraphData();
		@SuppressWarnings("unchecked")
		final IGraphGeneric<V, E> retVal = (IGraphGeneric<V, E>) (remove ? graphData.remove(key) : graphData.get(key));
		if (retVal == null){
			throw new IllegalArgumentException();
		}
		return retVal;
	}

	@Override
	protected <V, E> void putGraphData(IGraphKey<V, E> key, IGraphGeneric<V, E> data) {
		if (!key.isWriteAllowed()) throw new IllegalStateException();
		if (getGraphData().putIfAbsent(key, data) != null) throw new IllegalArgumentException();
	}
}
