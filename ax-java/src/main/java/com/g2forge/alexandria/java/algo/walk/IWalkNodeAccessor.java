package com.g2forge.alexandria.java.algo.walk;

import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.java.function.ISupplier;

public interface IWalkNodeAccessor<Node, Visitor> {
	public Collection<? extends Node> getChildren();

	public VisitResult post(ISupplier<? extends List<? extends Node>> context, Visitor visitor);

	public VisitResult pre(ISupplier<? extends List<? extends Node>> context, Visitor visitor);
}
