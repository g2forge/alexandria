package com.g2forge.alexandria.generic.type.java.typed;

import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IJavaGenericTyped<T> extends IJavaTyped, IGenericTyped<T, IJavaUntype> {}
