package com.g2forge.alexandria.record;

import java.util.function.Function;

import com.g2forge.alexandria.java.name.INamed;
import com.g2forge.alexandria.java.type.IGeneric;

public interface IFieldType<R, F> extends INamed<String>, IGeneric<F>, Function<R, IField<R, F>> {}
