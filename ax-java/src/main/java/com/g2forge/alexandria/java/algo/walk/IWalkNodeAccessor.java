package com.g2forge.alexandria.java.algo.walk;

import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.java.function.ISupplier;

/**
 * An accessor to be used by classes like {@link Walker}, which implement the {@link IWalker} interface, to traverse the nodes of the data structure, and report
 * events to the visitor. Each instance of this interface is bound to a specific node. Generally {@link IWalker} implementations will take a factory which
 * creates instances of this interface from objects of type <code>Node</code>.
 * 
 * @param <Node> The type of the types this walker can traverse. May be <code>Object</code>.
 * @param <Visitor> The visitor to send events to. A visitor will typically have domain specific methods for taking action when certain kinds of nodes are
 *            visited.
 */
public interface IWalkNodeAccessor<Node, Visitor> {
	/**
	 * Get the children of the node this accessor is bound to. Note that you are free to define what constitutes a "child". The data structure need not be a
	 * tree, it could be a graph, or really any data structure at all.
	 * 
	 * @return The children of the node this accessor is bound to.
	 */
	public Collection<? extends Node> getChildren();

	/**
	 * Report a post-visit event for this node. This method may call any method of the visitor that you desire in order to report that this node has been fully
	 * visited.
	 * 
	 * @param context The context in which this node was discovered - a supplier which provides a list of the nodes starting from the root, and ending with the
	 *            parent of this node.
	 * @param visitor The visitor.
	 * @return A {@link VisitResult} which will be used by the walker to control the traversal. Note that for obvious reasons {@link VisitResult#SkipChildren}
	 *         is not useful when returned by this method.
	 */
	public VisitResult post(ISupplier<? extends List<? extends Node>> context, Visitor visitor);

	/**
	 * Report a pre-visit event for this node. This method may call any method of the visitor that you desire in order to report that this node is now being
	 * visited, and its children (if any) will be visited next.
	 * 
	 * @param context The context in which this node was discovered - a supplier which provides a list of the nodes starting from the root, and ending with the
	 *            parent of this node.
	 * @param visitor The visitor.
	 * @return A {@link VisitResult} which will be used by the walker to control the traversal.
	 */
	public VisitResult pre(ISupplier<? extends List<? extends Node>> context, Visitor visitor);
}
