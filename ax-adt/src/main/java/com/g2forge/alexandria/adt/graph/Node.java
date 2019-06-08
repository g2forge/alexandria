package com.g2forge.alexandria.adt.graph;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
class Node<N> {
	protected final N node;

	protected Set<Edge<N>> in;

	protected Set<Edge<N>> out;
}