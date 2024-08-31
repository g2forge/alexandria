package com.g2forge.alexandria.adt.graph.v2;

import com.g2forge.alexandria.adt.collection.CollectionCollection;
import com.g2forge.alexandria.adt.collection.ICollection;
import com.g2forge.alexandria.java.function.builder.IModifier;

public interface IGraphModifier<V, E, Result> extends IGraphGeneric<V, E> {
	public interface IPathModifier<V, E, B> extends IModifier<B> {
		/**
		 * Add a new edge with the specified target vertex, and advance the source vertex to the specified target. Good for building linear paths.
		 * 
		 * @param edge The edge to add.
		 * @param target The target vertex for the edge.
		 * @return {@code this}
		 */
		public IPathModifier<V, E, B> add(E edge, V target);

		/**
		 * Add a new edge with the specified target vertex, and create a new modifier whose source is the specified target. Good for building branches off the
		 * path.
		 * 
		 * @param edge The edge to add.
		 * @param target The target vertex for the edge.
		 * @return A new path modifier whose source vertex is {@code target}
		 */
		public IPathModifier<V, E, B> branch(E edge, V target);
	}

	public Result edge(V source, E edge, V target);

	public IPathModifier<V, E, Result> path(V source);

	public default Result removeEdges(@SuppressWarnings("unchecked") E... edges) {
		return removeEdges(new CollectionCollection<>(edges));
	}

	public Result removeEdges(ICollection<E> edges);

	public Result removeVertices(ICollection<V> vertices);

	public default Result removeVertices(@SuppressWarnings("unchecked") V... vertices) {
		return removeVertices(new CollectionCollection<>(vertices));
	}

	public Result vertices(ICollection<V> vertices);

	public default Result vertices(@SuppressWarnings("unchecked") V... vertices) {
		return vertices(new CollectionCollection<>(vertices));
	}
}
