package com.g2forge.alexandria.command.stdio;

public interface IStandardIO<I, O> {
	public I getStandardInput();

	public O getStandardOutput();

	public O getStandardError();
}
