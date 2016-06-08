package com.g2forge.alexandria.generic.record;

import java.util.function.Function;

import com.g2forge.alexandria.java.name.INamed;
import com.g2forge.alexandria.java.typed.IGeneric;

public interface IFieldType<R, F> extends INamed<String>, IGeneric<F>, Function<R, IField<R, F>> {}
