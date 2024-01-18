package com.g2forge.alexandria.java.core.helpers;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.name.IStringNamed;
import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2G_I;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

public class TestHTree {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class Node implements IStringNamed {
		public static NodeBuilder named(String name) {
			return builder().name(name);
		}

		protected final String name;

		@Singular
		protected final List<Node> children;
	}

	protected void assertDFS(final Node root, final List<Node> expected, final boolean postorder) {
		Assert.assertEquals(expected, HTree.dfs(root, Node::getChildren, postorder).collect(Collectors.toList()));
	}

	protected void assertDFSWithParent(final Node root, final List<ITuple2G_<Node, Node>> expected, final boolean postorder) {
		Assert.assertEquals(expected, HTree.dfsWithParent(null, root, Node::getChildren, postorder).collect(Collectors.toList()));
	}

	@Test
	public void dfsChild() {
		final Node b, a = Node.named("a").child(b = Node.named("b").build()).build();
		assertDFS(a, HCollection.asList(a, b), false);
	}

	@Test
	public void dfsChildren() {
		final Node c, b, a = Node.named("a").child(b = Node.named("b").build()).child(c = Node.named("c").build()).build();
		assertDFS(a, HCollection.asList(a, b, c), false);
		assertDFS(a, HCollection.asList(c, b, a), true);
	}

	@Test
	public void dfsGrandchildren() {
		final Node d, c, b, a = Node.named("a").child(b = Node.named("b").child(c = Node.named("c").build()).child(d = Node.named("d").build()).build()).build();
		assertDFS(a, HCollection.asList(a, b, c, d), false);
		assertDFS(a, HCollection.asList(d, c, b, a), true);
	}

	@Test
	public void dfsSingle() {
		final Node a = Node.named("a").build();
		assertDFS(a, HCollection.asList(a), false);
	}

	@Test
	public void dfsWithParentChild() {
		final Node b, a = Node.named("a").child(b = Node.named("b").build()).build();
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(null, a), new Tuple2G_I<>(a, b)), false);
	}

	@Test
	public void dfsWithParentChildren() {
		final Node c, b, a = Node.named("a").child(b = Node.named("b").build()).child(c = Node.named("c").build()).build();
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(null, a), new Tuple2G_I<>(a, b), new Tuple2G_I<>(a, c)), false);
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(a, c), new Tuple2G_I<>(a, b), new Tuple2G_I<>(null, a)), true);
	}

	@Test
	public void dfsWithParentGrandchild() {
		final Node d, c, b, a = Node.named("a").child(b = Node.named("b").child(c = Node.named("c").build()).child(d = Node.named("d").build()).build()).build();
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(null, a), new Tuple2G_I<>(a, b), new Tuple2G_I<>(b, c), new Tuple2G_I<>(b, d)), false);
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(b, d), new Tuple2G_I<>(b, c), new Tuple2G_I<>(a, b), new Tuple2G_I<>(null, a)), true);
	}

	@Test
	public void dfsWithParentSingle() {
		final Node a = Node.named("a").build();
		assertDFSWithParent(a, HCollection.asList(new Tuple2G_I<>(null, a)), false);
	}
}
