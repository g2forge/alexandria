package com.g2forge.alexandria.command;

public interface IStandardIO<I, O> {
	public I getStandardInput();

	public O getStandardOutput();

	public O getStandardError();
}
