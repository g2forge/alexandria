package com.g2forge.alexandria.adt.record.v1;

import java.util.function.Function;

import com.g2forge.alexandria.java.adt.name.INamed;
import com.g2forge.alexandria.java.type.IGeneric;

public interface IFieldType<R, F> extends INamed<String>, IGeneric<F>, Function<R, IField<R, F>> {}
