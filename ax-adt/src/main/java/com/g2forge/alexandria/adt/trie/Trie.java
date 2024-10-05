package com.g2forge.alexandria.adt.trie;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.path.path.IPath;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public class Trie<KT, V> implements ITrie<KT, V> {
	protected Node<KT, V> root;

	@Override
	public IOptional<V> get(IPath<KT> path) {
		final Node<KT, V> root = getRoot();
		Node<KT, V> current;

		// Create an artificial parent of the root, if the root is labeled
		if ((root.getLabel() != null) && !root.getLabel().isEmpty()) {
			current = new Node<>(null);
			current.getChildren().put(getRoot().getLabel().getFirst(), getRoot());
		} else current = root;

		int index = 0;
		while (index < path.size()) {
			// Find the child
			final Node<KT, V> next = current.getChildren().get(path.getComponent(index));
			if (next == null) return NullableOptional.empty();

			// Ensure the label on the next node isn't longer than the path
			final int nextLabelSize = next.getLabel().size();
			if (path.size() < (index + nextLabelSize)) return NullableOptional.empty();

			// Ensure the label on the next node matches the path
			final IPath<KT> subPath = path.subPath(index, index + nextLabelSize);
			if (!subPath.equals(next.getLabel())) return NullableOptional.empty();

			current = next;
			index += nextLabelSize;
		}

		return current.isTerminal() ? NullableOptional.of(current.getValue()) : NullableOptional.empty();
	}

}
