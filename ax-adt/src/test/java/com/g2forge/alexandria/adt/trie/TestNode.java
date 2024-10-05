package com.g2forge.alexandria.adt.trie;

import java.util.HashMap;

import org.junit.Test;

import com.g2forge.alexandria.java.validate.IValidation;
import com.g2forge.alexandria.path.path.Path;
import com.g2forge.alexandria.test.HAssert;

public class TestNode {
	@Test
	public void terminalValue() {
		final IValidation validation = new Node<String, Object>(null, new HashMap<>(), false, new Object()).validate();
		HAssert.assertFalse(validation.isValid());
	}

	@Test
	public void childLabel() {
		final Node<String, Object> root = new Node<String, Object>(null);
		root.getChildren().put("a", new Node<>(new Path<>("b")));
		final IValidation validation = root.validate();
		HAssert.assertFalse(validation.isValid());
	}

	@Test
	public void circular() {
		final Node<String, Object> root = new Node<String, Object>(null);
		root.getChildren().put("a", root);
		final IValidation validation = root.validate();
		HAssert.assertFalse(validation.isValid());
	}
}
