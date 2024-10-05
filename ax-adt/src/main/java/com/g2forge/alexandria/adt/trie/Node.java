package com.g2forge.alexandria.adt.trie;

import java.util.HashMap;
import java.util.Map;

import com.g2forge.alexandria.path.path.IPath;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Node<KT, V> {
	protected final IPath<KT> label;

	protected final Map<KT, Node<KT, V>> children;

	protected boolean isTerminal;

	protected V value;

	public Node(IPath<KT> label) {
		this(label, new HashMap<>(), false, null);
	}

	public Node(IPath<KT> label, V value) {
		this(label, new HashMap<>(), true, value);
	}
}
