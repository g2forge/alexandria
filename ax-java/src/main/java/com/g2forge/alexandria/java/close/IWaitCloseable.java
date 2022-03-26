package com.g2forge.alexandria.java.close;

public interface IWaitCloseable extends ICloseable {
	public default ICloseable asCloseableWithWait() {
		return () -> {
			close();
			waitClosed();
		};
	}

	public void waitClosed();
}
