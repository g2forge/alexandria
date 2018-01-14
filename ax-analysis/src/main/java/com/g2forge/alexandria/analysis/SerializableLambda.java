package com.g2forge.alexandria.analysis;

interface SerializableLambda extends ISerializableLambda {
	@Override
	public default IMethodAnalyzer asMethodAnalyzer() {
		return new MethodAnalyzer(this);
	}
}