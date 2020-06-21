package com.g2forge.alexandria.java.io.dataaccess;

import java.io.OutputStream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OutputStreamDataSink extends AOutputStreamDataSink {
	protected final OutputStream stream;
}
