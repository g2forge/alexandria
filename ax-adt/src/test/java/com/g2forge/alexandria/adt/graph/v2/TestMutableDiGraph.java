package com.g2forge.alexandria.adt.graph.v2;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;

public class TestMutableDiGraph {
	@Test
	public void basic() {
		final Vertex a = new Vertex("a"), b = new Vertex("b");
		final Edge x = new Edge("x");
		final IMutableDiGraph<Vertex, Edge> graph = new MutableDiGraph<Vertex, Edge>();

		HAssert.assertEquals(HCollection.emptySet(), graph.getVertices());
		HAssert.assertEquals(HCollection.emptySet(), graph.getEdges());

		graph.vertices(a, b);
		HAssert.assertEquals(HCollection.asSet(a, b), graph.getVertices());
		HAssert.assertEquals(HCollection.emptySet(), graph.getEdges());
		HAssert.assertEquals(0, graph.getVertex(a).getDegree());
		HAssert.assertEquals(0, graph.getVertex(b).getDegree());

		graph.edge(a, x, b);
		HAssert.assertEquals(HCollection.asSet(a, b), graph.getVertices());
		HAssert.assertEquals(HCollection.asSet(x), graph.getEdges());
		HAssert.assertEquals(0, graph.getVertex(a).getInDegree());
		HAssert.assertEquals(HCollection.asSet(x), graph.getVertex(a).getOutgoingEdges());
		HAssert.assertEquals(HCollection.asSet(x), graph.getVertex(b).getIncomingEdges());
		HAssert.assertSame(a, graph.getEdge(x).getSource());
		HAssert.assertSame(b, graph.getEdge(x).getTarget());

		graph.removeVertices(b);
		HAssert.assertEquals(HCollection.asSet(a), graph.getVertices());
		HAssert.assertEquals(HCollection.emptySet(), graph.getEdges());
		HAssert.assertEquals(0, graph.getVertex(a).getDegree());
	}
}
