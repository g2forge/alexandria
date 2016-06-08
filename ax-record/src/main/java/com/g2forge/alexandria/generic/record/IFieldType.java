package com.g2forge.alexandria.generic.record;

import java.util.function.Function;

import com.g2forge.alexandria.generic.java.typed.IGeneric;
import com.g2forge.alexandria.java.name.INamed;

public interface IFieldType<R, F> extends INamed<String>, IGeneric<F>, Function<R, IField<R, F>> {}
