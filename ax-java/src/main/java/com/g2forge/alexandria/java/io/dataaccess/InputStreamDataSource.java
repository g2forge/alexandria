package com.g2forge.alexandria.java.io.dataaccess;

import java.io.InputStream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InputStreamDataSource extends AInputStreamDataSource {
	protected final InputStream stream;
}