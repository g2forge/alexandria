package com.g2forge.alexandria.adt.graph.v2;

import java.util.Set;

public interface IGraph<V, E> extends IGraphGeneric<V, E> {
	public interface IEdgeAccessor<V, E> extends IGraphGeneric<V, E> {
		public E getEdge();

		public V getSource();

		public V getTarget();
	}

	public interface IVertexAccessor<V, E> extends IGraphGeneric<V, E> {
		public int getDegree();

		public Set<E> getEdges();

		public V getVertex();
	}

	public IEdgeAccessor<? extends V, ? extends E> getEdge(E edge);

	public Set<E> getEdges();

	public IVertexAccessor<? extends V, ? extends E> getVertex(V vertex);

	public Set<V> getVertices();
}
