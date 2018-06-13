package com.g2forge.alexandria.command.stdio;

import java.io.InputStream;
import java.io.PrintStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StandardIO<I, O> implements IStandardIO<I, O> {
	public static final StandardIO<InputStream, PrintStream> of() {
		final StandardIO.StandardIOBuilder<InputStream, PrintStream> retVal = StandardIO.builder();
		retVal.standardInput(System.in);
		retVal.standardOutput(System.out);
		retVal.standardError(System.err);
		return retVal.build();
	}

	protected final I standardInput;

	protected final O standardOutput;

	protected final O standardError;
}
