package com.g2forge.alexandria.test;

public class MockedCodeError extends Error {
	private static final long serialVersionUID = 7820622397289970644L;

	public MockedCodeError() {}

	public MockedCodeError(String message) {
		super(message);
	}

	public MockedCodeError(String message, Throwable cause) {
		super(message, cause);
	}

	public MockedCodeError(Throwable cause) {
		super(cause);
	}
}
