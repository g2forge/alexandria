package com.g2forge.alexandria.adt.trie;

import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.test.HAssert;

public class TestTrie3Node {
	protected static final Trie<Character, String> trie = new Trie<>(new NodeBuilder("t", null).children(c -> {
		c.child("est", "test");
		c.child("oast", "toast");
	}).build());

	@Test
	public void validate() {
		HAssert.assertTrue(trie.root.validate().isValid());
	}

	@Test
	public void roast() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("roast"));
		HAssert.assertFalse(result.isNotEmpty());
	}

	@Test
	public void temp() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("temp"));
		HAssert.assertFalse(result.isNotEmpty());
	}

	@Test
	public void test() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("test"));
		HAssert.assertTrue(result.isNotEmpty());
		HAssert.assertEquals("test", result.get());
	}

	@Test
	public void toast() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("toast"));
		HAssert.assertTrue(result.isNotEmpty());
		HAssert.assertEquals("toast", result.get());
	}

	@Test
	public void toaster() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("toaster"));
		HAssert.assertFalse(result.isNotEmpty());
	}

	@Test
	public void toasting() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("toasting"));
		HAssert.assertFalse(result.isNotEmpty());
	}

	@Test
	public void toasti() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("toasti"));
		HAssert.assertFalse(result.isNotEmpty());
	}

	@Test
	public void trip() {
		final IOptional<String> result = trie.get(NodeBuilder.toLabel("trip"));
		HAssert.assertFalse(result.isNotEmpty());
	}
}