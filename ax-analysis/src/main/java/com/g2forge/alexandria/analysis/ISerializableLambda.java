package com.g2forge.alexandria.analysis;

import java.io.Serializable;

public interface ISerializableLambda extends Serializable {
	public IMethodAnalyzer asMethodAnalyzer();
}