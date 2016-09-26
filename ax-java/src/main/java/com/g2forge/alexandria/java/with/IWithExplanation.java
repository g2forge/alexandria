package com.g2forge.alexandria.java.with;

import com.g2forge.alexandria.java.function.ISupplier;
import com.g2forge.alexandria.java.name.IDescribed;

public interface IWithExplanation<T, E extends IDescribed<String>> extends ISupplier<T> {
	public E getExplanation();
}
