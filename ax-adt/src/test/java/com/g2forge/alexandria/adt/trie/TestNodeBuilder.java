package com.g2forge.alexandria.adt.trie;

import org.junit.Test;

import com.g2forge.alexandria.path.path.Path;
import com.g2forge.alexandria.test.HAssert;

public class TestNodeBuilder {
	@Test
	public void root() {
		HAssert.assertEquals(new Node<>(new Path<Character>()), new NodeBuilder("", null).build());;;
	}

	@Test
	public void label() {
		HAssert.assertEquals(new Node<>(new Path<Character>('a', 'b', 'c')), new NodeBuilder("abc", null).build());;;
	}

	@Test
	public void child() {
		final Node<Character, String> expected = new Node<>(new Path<Character>('a', 'b', 'c'));
		expected.getChildren().put('d', new Node<>(new Path<Character>('d'), "value"));
		HAssert.assertEquals(expected, new NodeBuilder("abc", null).children(c -> c.child("d", "value")).build());;;
	}
}
