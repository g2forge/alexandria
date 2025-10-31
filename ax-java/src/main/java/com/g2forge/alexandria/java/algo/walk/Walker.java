package com.g2forge.alexandria.java.algo.walk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Walker<Node, Visitor> {
	@Getter(AccessLevel.PUBLIC)
	protected final IFunction1<Node, IWalkNodeAccessor<Node, Visitor>> accessorFunction;

	public void walk(Node root, Visitor visitor) {
		final Stack<Node> stack = new Stack<>();
		final LinkedList<Step<Node>> queue = new LinkedList<>();

		queue.add(new Step<>(StepType.Pre, root));
		while (!queue.isEmpty()) {
			final Step<Node> currentStep = queue.remove();
			final boolean isPre = StepType.Pre.equals(currentStep.getType());
			if (!isPre && (stack.pop() != currentStep.getNode())) throw new IllegalStateException("Post step node did not match top of stack!");

			final IWalkNodeAccessor<Node, Visitor> accessor = getAccessorFunction().apply(currentStep.getNode());
			final ISupplier<List<? extends Node>> context = () -> Collections.unmodifiableList(stack);
			final VisitResult visitResult;
			switch (currentStep.getType()) {
				case Pre:
					visitResult = accessor.pre(context, visitor);
					break;
				case Post:
					visitResult = accessor.post(context, visitor);
					break;
				default:
					throw new EnumException(StepType.class, currentStep.getType());
			}

			final List<Step<Node>> inserts = new ArrayList<>();
			switch (visitResult) {
				case Continue:
					if (isPre) inserts.addAll(accessor.getChildren().stream().map(c -> new Step<Node>(StepType.Pre, c)).toList());
				case SkipChildren:
					if (isPre) {
						stack.push(currentStep.getNode());
						inserts.add(new Step<>(StepType.Post, currentStep.getNode()));
					}
					break;
				case Terminate:
					return;
				case SkipSiblings:
					if (stack.isEmpty()) {
						if (!queue.isEmpty()) throw new IllegalStateException();
					} else {
						final Node parent = stack.peek();
						while (!queue.isEmpty()) {
							final Step<Node> removed = queue.removeFirst();
							if (removed.getNode() == parent) {
								if (!StepType.Post.equals(removed.getType())) throw new IllegalStateException();
								queue.addFirst(removed);
								break;
							}
						}
					}
					break;
			}
			queue.addAll(0, inserts);
		}
	}
}