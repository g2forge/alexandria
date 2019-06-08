package com.g2forge.alexandria.java.with;

import com.g2forge.alexandria.java.adt.name.IDescribed;
import com.g2forge.alexandria.java.function.ISupplier;

public interface IWithExplanation<T, E extends IDescribed<String>> extends ISupplier<T> {
	public E getExplanation();
}
