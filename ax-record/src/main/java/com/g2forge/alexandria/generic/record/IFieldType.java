package com.g2forge.alexandria.generic.record;

import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.generic.java.name.INamed;
import com.g2forge.alexandria.generic.java.typed.IGeneric;

public interface IFieldType<R, F> extends INamed<String>, IGeneric<F>, IMap1<R, IField<R, F>> {}
