package com.g2forge.alexandria.adt.graph.v2;

import java.util.LinkedHashSet;

import com.g2forge.alexandria.adt.collection.ICollection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MutableDiGraph<V, E> extends DiGraph<V, E> implements IMutableDiGraph<V, E> {
	@RequiredArgsConstructor
	@Getter(AccessLevel.PROTECTED)
	protected static class PathModifier<V, E> implements IPathModifier<V, E, Void> {
		protected final IPathModifier<V, E, ?> modifier;

		@Override
		public IPathModifier<V, E, Void> add(E edge, V target) {
			return self(getModifier().add(edge, target));
		}

		@Override
		public IPathModifier<V, E, Void> branch(E edge, V target) {
			return self(getModifier().branch(edge, target));
		}

		@Override
		public Void done() {
			getModifier().done();
			return null;
		}

		protected IPathModifier<V, E, Void> self(final IPathModifier<V, E, ?> retVal) {
			return retVal == getModifier() ? this : new PathModifier<>(retVal);
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final IGraphModifier<V, E, ?> modifier = new DiGraphBuilder<>(this);

	public MutableDiGraph() {
		super(new MutableGraphKey<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
	}

	@Override
	public Void edge(V source, E edge, V target) {
		getModifier().edge(source, edge, target);
		return null;
	}

	@Override
	public IPathModifier<V, E, Void> path(V source) {
		return new PathModifier<V, E>(MutableDiGraph.this.getModifier().path(source));
	}

	@Override
	public Void removeEdges(ICollection<E> edges) {
		getModifier().removeEdges(edges);
		return null;
	}

	@Override
	public Void removeVertices(ICollection<V> vertices) {
		getModifier().removeVertices(vertices);
		return null;
	}

	@Override
	public Void vertices(ICollection<V> vertices) {
		getModifier().vertices(vertices);
		return null;
	}
}
