package com.g2forge.alexandria.java.typeswitch;

public interface ITypedFunction1<I, O> {
	public <_I> O apply(_I input);

	public Class<? super I> getInputType();

	public <_I> boolean isApplicable(_I input);
}
