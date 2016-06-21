package com.g2forge.alexandria.generic.type.java.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JavaScope {
	Static(false, true),
	Instance(false, false),
	Inherited(true, false);

	@Getter
	protected final boolean inherited;

	@Getter
	protected final boolean statics;
}