package com.g2forge.alexandria.java.algo.walk;

public enum VisitResult {
	/** Continue DFS traversal as normal. */
	Continue,

	/** Step immediately after this call. */
	Terminate,

	/** Don't visit any children of this node. This is only meaningful during a pre-visit, and is treated the same as {@link #Continue} otherwise. */
	SkipChildren,

	/**
	 * Don't visit any further siblings of this node. If returned from a pre-visit, then it will also skip the children of this node, and will not post-visit
	 * the node.
	 */
	SkipSiblings;
}