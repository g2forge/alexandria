package com.g2forge.alexandria.adt.trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.java.validate.CompositeValidation;
import com.g2forge.alexandria.java.validate.IValidatable;
import com.g2forge.alexandria.java.validate.IValidation;
import com.g2forge.alexandria.path.path.IPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Node<KT, V> implements IValidatable {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class ChildLabelValidation<KT> implements IValidation {
		protected final KT key;

		protected final IPath<KT> label;

		@Override
		public boolean isValid() {
			return getLabel() != null && getKey().equals(getLabel().getFirst());
		}
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class EmptyValueValidation<V> implements IValidation {
		protected final boolean isTerminal;

		protected final V value;

		@Override
		public boolean isValid() {
			return isTerminal() || getValue() == null;
		}
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class NonCircularValidation<KT> implements IValidation {
		protected final Map<? extends Node<KT, ?>, ?> ancestors;

		protected final Node<KT, ?> node;

		@Override
		public boolean isValid() {
			return !getAncestors().containsKey(getNode());
		}
	}

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

	@Override
	public IValidation validate() {
		final Object value = new Object();
		final IdentityHashMap<Node<KT, V>, Object> ancestors = new IdentityHashMap<>();
		final CompositeValidation.CompositeValidationBuilder retVal = CompositeValidation.builder();

		final LinkedList<Node<KT, V>> queue = new LinkedList<Node<KT, V>>();
		queue.add(this);
		while (!queue.isEmpty()) {
			final Node<KT, V> current = queue.removeFirst();
			// See if we're done with all the descendants of the current node
			if (ancestors.remove(current) != null) continue;
			ancestors.put(current, value);

			retVal.validation(new EmptyValueValidation<>(isTerminal(), getValue()));

			final List<Node<KT, V>> children = new ArrayList<>();
			for (Map.Entry<KT, Node<KT, V>> entry : current.getChildren().entrySet()) {
				retVal.validation(new ChildLabelValidation<>(entry.getKey(), entry.getValue().getLabel()));
				final NonCircularValidation<KT> nonCircularValidation = new NonCircularValidation<>(new IdentityHashMap<>(ancestors), entry.getValue());
				retVal.validation(nonCircularValidation);
				if (nonCircularValidation.isValid()) children.add(entry.getValue());
			}

			// Queue the children, but queue this node first to mark the end of the children
			Collections.reverse(children);
			queue.addFirst(current);
			children.stream().forEach(queue::addFirst);
		}

		return retVal.build();
	}
}
