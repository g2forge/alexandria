package com.g2forge.alexandria.java.debug;

import lombok.AccessLevel;
import lombok.Getter;

public class Trace {
	@Getter(AccessLevel.PROTECTED)
	protected final Throwable throwable;

	public Trace() {
		this.throwable = DebugHelpers.isDebugEnabled() ? new Throwable("Creation point") : null;
	}

	public <T extends Throwable> T addTrace(T throwable) {
		final Throwable mine = getThrowable();
		if (mine != null) throwable.addSuppressed(mine);
		return throwable;
	}
}
