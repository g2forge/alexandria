package com.g2forge.alexandria.adt.graph.v2;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;

public class TestDiGraph {
	@Test
	public void basic() {
		final Vertex a = new Vertex("a"), b = new Vertex("b");
		final Edge x = new Edge("x");
		final IDiGraph<Vertex, Edge> graph = new DiGraphBuilder<Vertex, Edge>().vertices(a, b).edge(a, x, b).build();

		HAssert.assertEquals(HCollection.asSet(a, b), graph.getVertices());
		HAssert.assertEquals(HCollection.asSet(x), graph.getEdges());
		HAssert.assertEquals(0, graph.getVertex(a).getInDegree());
		HAssert.assertEquals(HCollection.asSet(x), graph.getVertex(a).getOutgoingEdges());
		HAssert.assertEquals(HCollection.asSet(x), graph.getVertex(b).getIncomingEdges());
		HAssert.assertSame(a, graph.getEdge(x).getSource());
		HAssert.assertSame(b, graph.getEdge(x).getTarget());
	}

	@Test
	public void remove() {
		final Vertex a = new Vertex("a"), b = new Vertex("b");
		final Edge x = new Edge("x");
		final IDiGraph<Vertex, Edge> graph = new DiGraphBuilder<Vertex, Edge>().vertices(a, b).edge(a, x, b).removeVertices(a).build();
		HAssert.assertEquals(HCollection.asSet(b), graph.getVertices());
		HAssert.assertEquals(HCollection.emptySet(), graph.getEdges());
	}
}
