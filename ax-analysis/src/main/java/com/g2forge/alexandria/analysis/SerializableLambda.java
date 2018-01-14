package com.g2forge.alexandria.analysis;

import java.io.Serializable;

interface SerializableLambda extends Serializable {
	public default IMethodAnalyzer asMethodAnalyzer() {
		return new MethodAnalyzer(this);
	}
}