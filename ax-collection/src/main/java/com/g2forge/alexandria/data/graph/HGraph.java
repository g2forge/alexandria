package com.g2forge.alexandria.data.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.IFunction1;

public class HGraph {
	public static <N> List<N> toposort(Collection<N> nodes, Function<N, Set<N>> accessor, boolean isOut) {
		final Map<N, Node<N>> nodeMap = nodes.stream().map(n -> {
			final Node<N> retVal = new Node<>(n);
			if (isOut) retVal.setIn(new HashSet<>());
			else retVal.setOut(new HashSet<>());
			return retVal;
		}).collect(Collectors.toMap(Node::getNode, IFunction1.identity(), HCollector.mergeFail(), HashMap::new));
		for (N n : nodes) {
			final Node<N> node = nodeMap.get(n);
			final Set<Edge<N>> edges = accessor.apply(n).stream().map(o -> {
				final Node<N> other = nodeMap.get(o);
				if (other == null) throw new NullPointerException(String.format("Node \"%s\" has a broken edge to \"%s\"!", node.getNode(), o));
				return new Edge<N>(isOut ? node : other, isOut ? other : node);
			}).collect(Collectors.toSet());
			if (isOut) node.setOut(edges);
			else node.setIn(edges);
			edges.stream().forEach(e -> (isOut ? e.getTo().getIn() : e.getFrom().getOut()).add(e));
		}

		final List<Node<N>> retVal = new ArrayList<>();
		final Set<Node<N>> noIncoming = new HashSet<>(nodeMap.values().stream().filter(node -> node.getIn().isEmpty()).collect(Collectors.toSet()));

		// While there are more nodes with no incoming edges
		while (!noIncoming.isEmpty()) {
			// Grab a node
			final Node<N> node = HCollection.removeAny(noIncoming);
			// Put it into the result
			retVal.add(node);

			// Remove all the outbound edges from the graph
			node.getOut().stream().forEach(e -> {
				final Set<Edge<N>> in = e.getTo().getIn();
				in.remove(e);
				// If we just make the to node have no more incoming edges...
				if (in.isEmpty()) noIncoming.add(e.getTo());
			});
			node.setOut(new HashSet<>());
		}

		// Check to see if there's a cycle
		final List<N> cyclic = nodeMap.values().stream().filter(node -> !node.getIn().isEmpty()).map(Node::getNode).collect(Collectors.toList());
		if (!cyclic.isEmpty()) throw new IllegalArgumentException("Input graph is cyclic, remaining nodes are: " + cyclic);

		return retVal.stream().map(Node::getNode).collect(Collectors.toList());
	}

}
