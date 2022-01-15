package com.g2forge.alexandria.adt.graph.v2;

import java.util.Collections;
import java.util.Set;

import com.g2forge.alexandria.adt.graph.v2.member.IGraphKey;
import com.g2forge.alexandria.adt.graph.v2.member.IMemberDataStrategy;
import com.g2forge.alexandria.adt.graph.v2.member.MemberDataStrategy;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiGraph<V, E> implements IDiGraph<V, E> {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class EdgeData<V, E> implements IGraphGeneric<V, E> {
		protected final V source;

		protected final V target;
	}

	protected interface ILockableGraphKey<V, E> extends IGraphKey<V, E> {
		public void lock();
	}

	protected static class ImmutableGraphKey<V, E> implements ILockableGraphKey<V, E> {
		@Getter
		protected boolean writeAllowed = true;

		@Override
		public boolean isReadAllowed() {
			return true;
		}

		@Override
		public void lock() {
			writeAllowed = false;
		}
	}

	protected static class MutableGraphKey<V, E> implements ILockableGraphKey<V, E> {
		@Override
		public boolean isReadAllowed() {
			return true;
		}

		@Override
		public boolean isWriteAllowed() {
			return true;
		}

		@Override
		public void lock() {}
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class VertexData<V, E> implements IGraphGeneric<V, E> {
		protected final Set<E> incomingEdges;

		protected final Set<E> outgoingEdges;
	}

	@Getter(AccessLevel.PROTECTED)
	protected final ILockableGraphKey<V, E> key;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final IMemberDataStrategy<V, E, V, VertexData<V, E>> vertexDataStrategy = new MemberDataStrategy<>(getKey());

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final IMemberDataStrategy<V, E, E, EdgeData<V, E>> edgeDataStrategy = new MemberDataStrategy<>(getKey());

	protected final Set<V> verticesRaw;

	protected final Set<E> edgesRaw;

	@Getter(lazy = true)
	private final Set<V> vertices = Collections.unmodifiableSet(getVerticesRaw());

	@Getter(lazy = true)
	private final Set<E> edges = Collections.unmodifiableSet(getEdgesRaw());

	@Override
	public IEdgeAccessor<? extends V, ? extends E> getEdge(E edge) {
		return new IEdgeAccessor<V, E>() {
			@Getter(lazy = true, value = AccessLevel.PROTECTED)
			private final EdgeData<V, E> data = getEdgeDataStrategy().get(getEdge());

			@Override
			public E getEdge() {
				return edge;
			}

			@Override
			public V getSource() {
				return getData().getSource();
			}

			@Override
			public V getTarget() {
				return getData().getTarget();
			}
		};
	}

	@Override
	public IVertexAccessor<? extends V, ? extends E> getVertex(V vertex) {
		return new IVertexAccessor<V, E>() {
			@Getter(lazy = true, value = AccessLevel.PROTECTED)
			private final VertexData<V, E> data = getVertexDataStrategy().get(getVertex());

			@Override
			public int getDegree() {
				return getInDegree() + getOutDegree();
			}

			@Override
			public Set<E> getEdges() {
				return Collections.unmodifiableSet(HCollection.union(getIncomingEdges(), getOutgoingEdges()));
			}

			@Override
			public Set<E> getIncomingEdges() {
				return Collections.unmodifiableSet(getData().getIncomingEdges());
			}

			@Override
			public int getInDegree() {
				final Set<E> incomingEdges = getIncomingEdges();
				if (incomingEdges == null) return 0;
				return incomingEdges.size();
			}

			@Override
			public int getOutDegree() {
				final Set<E> outgoingEdges = getOutgoingEdges();
				if (outgoingEdges == null) return 0;
				return outgoingEdges.size();
			}

			@Override
			public Set<E> getOutgoingEdges() {
				return Collections.unmodifiableSet(getData().getOutgoingEdges());
			}

			@Override
			public V getVertex() {
				return vertex;
			}
		};
	}
}
