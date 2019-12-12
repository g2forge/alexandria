package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayOutputStream;

import lombok.Getter;

@Getter
public class ByteArrayDataSink extends AOutputStreamDataSink {
	protected final ByteArrayOutputStream stream = new ByteArrayOutputStream();
}