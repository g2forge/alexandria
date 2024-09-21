package com.g2forge.alexandria.adt.graph.v2;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.g2forge.alexandria.adt.graph.v2.DiGraph.EdgeData;
import com.g2forge.alexandria.adt.graph.v2.DiGraph.ILockableGraphKey;
import com.g2forge.alexandria.adt.graph.v2.DiGraph.ImmutableGraphKey;
import com.g2forge.alexandria.adt.graph.v2.DiGraph.VertexData;
import com.g2forge.alexandria.adt.graph.v2.member.IMemberDataStrategy;
import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.collection.ICollection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiGraphBuilder<V, E> implements IGraphBuilder<V, E, DiGraphBuilder<V, E>> {
	@AllArgsConstructor
	protected class PathModifier implements IPathModifier<V, E, DiGraphBuilder<V, E>> {
		protected V source;

		@Override
		public IPathModifier<V, E, DiGraphBuilder<V, E>> add(E edge, V target) {
			edge(source, edge, target);
			source = target;
			return this;
		}

		@Override
		public IPathModifier<V, E, DiGraphBuilder<V, E>> branch(E edge, V target) {
			edge(source, edge, target);
			return new PathModifier(target);
		}

		@Override
		public DiGraphBuilder<V, E> done() {
			return self();
		}
	}

	protected final DiGraph<V, E> graph;

	public DiGraphBuilder() {
		this.graph = new DiGraph<V, E>(new ImmutableGraphKey<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
	}

	@Override
	public IDiGraph<V, E> build() {
		final ILockableGraphKey<V, E> key = getGraph().getKey();
		if (!key.isWriteAllowed()) throw new IllegalStateException();
		key.lock();
		return getGraph();
	}

	@Note(type = NoteType.TODO, value = "Create and use multi-assertion or validation API")
	@Override
	public DiGraphBuilder<V, E> edge(V source, E edge, V target) {
		final DiGraph<V, E> graph = getGraph();
		if (!graph.getKey().isWriteAllowed()) throw new IllegalStateException();

		final Set<V> vertices = graph.getVerticesRaw();
		if (!vertices.contains(source)) throw new IllegalArgumentException("Source vertex is not a member of the graph");
		if (!vertices.contains(target)) throw new IllegalArgumentException("Target vertex is not a memeber of the graph");

		final Set<E> edges = graph.getEdgesRaw();
		if (edges.contains(edge)) throw new IllegalArgumentException("Edge is already a member of the graph");
		edges.add(edge);

		final IMemberDataStrategy<V, E, V, VertexData<V, E>> vertexDataStrategy = graph.getVertexDataStrategy();
		final IMemberDataStrategy<V, E, E, EdgeData<V, E>> edgeDataStrategy = graph.getEdgeDataStrategy();

		vertexDataStrategy.get(source).getOutgoingEdges().add(edge);
		edgeDataStrategy.put(edge, new DiGraph.EdgeData<>(source, target));
		vertexDataStrategy.get(target).getIncomingEdges().add(edge);

		return self();
	}

	@Override
	public IPathModifier<V, E, DiGraphBuilder<V, E>> path(V source) {
		final DiGraph<V, E> graph = getGraph();
		if (!graph.getKey().isWriteAllowed()) throw new IllegalStateException();
		if (!graph.getVerticesRaw().contains(source)) throw new IllegalArgumentException();
		return new PathModifier(source);
	}

	protected DiGraphBuilder<V, E> removeEdges(Collection<? extends E> edges) {
		final DiGraph<V, E> graph = getGraph();
		final IMemberDataStrategy<V, E, V, VertexData<V, E>> vertexDataStrategy = graph.getVertexDataStrategy();
		final IMemberDataStrategy<V, E, E, EdgeData<V, E>> edgeDataStrategy = graph.getEdgeDataStrategy();

		final Set<E> existing = graph.getEdgesRaw();
		for (E edge : edges) {
			if (existing.remove(edge)) {
				final DiGraph.EdgeData<V, E> edgeData = edgeDataStrategy.remove(edge);
				vertexDataStrategy.get(edgeData.getSource()).getOutgoingEdges().remove(edge);
				vertexDataStrategy.get(edgeData.getTarget()).getIncomingEdges().remove(edge);
			}
		}
		return self();
	}

	@Override
	public DiGraphBuilder<V, E> removeEdges(ICollection<E> edges) {
		if (!getGraph().getKey().isWriteAllowed()) throw new IllegalStateException();
		return removeEdges(edges.toCollection());
	}

	@Override
	public DiGraphBuilder<V, E> removeVertices(ICollection<V> vertices) {
		final DiGraph<V, E> graph = getGraph();
		if (!graph.getKey().isWriteAllowed()) throw new IllegalStateException();

		final IMemberDataStrategy<V, E, V, VertexData<V, E>> vertexDataStrategy = graph.getVertexDataStrategy();

		{ // Remove all the incident edges
			final Set<E> toRemove = new HashSet<>();
			for (V vertex : vertices) {
				final DiGraph.VertexData<V, E> vertexData = vertexDataStrategy.get(vertex);
				toRemove.addAll(vertexData.getIncomingEdges());
				toRemove.addAll(vertexData.getOutgoingEdges());
			}
			removeEdges(toRemove);
		}

		// Now we can remove the vertices
		final Set<V> existingVertices = graph.getVerticesRaw();
		for (V vertex : vertices) {
			if (existingVertices.remove(vertex)) vertexDataStrategy.remove(vertex);
		}
		return self();
	}

	protected DiGraphBuilder<V, E> self() {
		return this;
	}

	@Override
	public DiGraphBuilder<V, E> vertices(ICollection<V> vertices) {
		final DiGraph<V, E> graph = getGraph();
		if (!graph.getKey().isWriteAllowed()) throw new IllegalStateException();

		final IMemberDataStrategy<V, E, V, VertexData<V, E>> vertexDataStrategy = graph.getVertexDataStrategy();

		final Set<V> existing = graph.getVerticesRaw();
		for (V vertex : vertices) {
			existing.add(vertex);
			vertexDataStrategy.put(vertex, new DiGraph.VertexData<>(new HashSet<>(), new HashSet<>()));
		}
		return self();
	}
}
