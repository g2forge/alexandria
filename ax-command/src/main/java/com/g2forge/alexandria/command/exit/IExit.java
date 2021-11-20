package com.g2forge.alexandria.command.exit;

public interface IExit {
	public int getCode();

	public default boolean isSuccess() {
		return getCode() == 0;
	}
}
