package com.g2forge.alexandria.adt.graph;

import lombok.Data;

@Data
class Edge<N> {
	protected final Node<N> from;

	protected final Node<N> to;
}