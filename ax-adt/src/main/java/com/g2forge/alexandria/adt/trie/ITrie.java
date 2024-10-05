package com.g2forge.alexandria.adt.trie;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.path.path.IPath;

public interface ITrie<KT, V> {
	public IOptional<V> get(IPath<KT> path);
}
