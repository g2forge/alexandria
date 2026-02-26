package com.g2forge.alexandria.java.algo.walk;

/**
 * A walker is responsible for walking a data structure consisting of nodes organized into a tree-like structure, and sending events to a visitor.
 * Implementations of this class will generally take an factory which converts nodes to instances of {@link IWalkNodeAccessor} as a constructor argument to tell
 * the walker how to interact with the nodes and the visitor. It is common for walkers to be singletons, or to be constructed with reference to some kind of
 * parallel or distributed framework.
 * 
 * @param <Node> The type of the types this walker can traverse. May be <code>Object</code>.
 * @param <Visitor> The visitor to send events to. A visitor will typically have domain specific methods for taking action when certain kinds of nodes are
 *            visited.
 * @see Walker
 */
public interface IWalker<Node, Visitor> {
	/**
	 * Walk the data structure from the given root, and report events to the given visitor.
	 * 
	 * @param root The root node of the walk.
	 * @param visitor The visitor to send events to.
	 */
	public void walk(Node root, Visitor visitor);
}
