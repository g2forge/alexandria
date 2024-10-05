package com.g2forge.alexandria.adt.trie;

import com.g2forge.alexandria.java.core.helpers.HArray;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

public class NodeBuilder implements IBuilder<Node<Character, String>> {
	public interface IChildBuilder {
		public NodeBuilder child(String label, String value);
	}

	public static IPath<Character> toLabel(String label) {
		return new Path<>(HArray.toObject(label.toCharArray()));
	}

	protected final Node<Character, String> node;

	public NodeBuilder(String label, String value) {
		if (value == null) this.node = new Node<>(toLabel(label));
		else this.node = new Node<>(toLabel(label), value);
	}

	@Override
	public Node<Character, String> build() {
		return node;
	}

	public NodeBuilder children(IConsumer1<NodeBuilder.IChildBuilder> consumer) {
		consumer.accept(new IChildBuilder() {
			@Override
			public NodeBuilder child(String label, String value) {
				final NodeBuilder retVal = new NodeBuilder(label, value);
				node.getChildren().put(retVal.node.getLabel().getFirst(), retVal.node);
				return retVal;
			}
		});
		return this;
	}
}