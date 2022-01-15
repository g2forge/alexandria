package com.g2forge.alexandria.adt.graph.v2;

import com.g2forge.alexandria.java.function.builder.IBuilder;

public interface IGraphBuilder<V, E, B extends IGraphBuilder<V, E, B>> extends IBuilder<IGraph<V, E>>, IGraphModifier<V, E, B> {}
