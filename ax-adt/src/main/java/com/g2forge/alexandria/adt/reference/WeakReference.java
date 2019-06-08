package com.g2forge.alexandria.adt.reference;

import java.lang.ref.ReferenceQueue;

public class WeakReference<T> extends java.lang.ref.WeakReference<T> implements IReference<T> {
	public WeakReference(T referent) {
		super(referent);
	}

	public WeakReference(T referent, ReferenceQueue<? super T> q) {
		super(referent, q);
	}
}
