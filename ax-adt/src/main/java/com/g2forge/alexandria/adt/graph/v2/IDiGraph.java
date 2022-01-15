package com.g2forge.alexandria.adt.graph.v2;

import java.util.Set;

public interface IDiGraph<V, E> extends IGraph<V, E> {
	public interface IVertexAccessor<V, E> extends IGraph.IVertexAccessor<V, E> {
		public Set<E> getIncomingEdges();

		public int getInDegree();

		public int getOutDegree();

		public Set<E> getOutgoingEdges();
	}

	@Override
	public IVertexAccessor<? extends V, ? extends E> getVertex(V vertex);
}
