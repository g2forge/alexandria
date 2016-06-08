package com.g2forge.alexandria.generic.type.java.typed;

import com.g2forge.alexandria.generic.java.typed.IGenericTyped;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;

public interface IJavaGenericTyped<T> extends IJavaTyped, IGenericTyped<T, IJavaUntype> {}
