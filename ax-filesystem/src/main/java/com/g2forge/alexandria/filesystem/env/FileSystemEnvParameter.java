package com.g2forge.alexandria.filesystem.env;

import java.util.HashMap;
import java.util.Map;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class FileSystemEnvParameter<T> {
	protected final String key;

	protected final ITypeRef<T> type;

	public Map<String, ?> create(T value) {
		final Map<String, Object> retVal = new HashMap<>();
		put(retVal, value);
		return retVal;
	}

	public T get(Map<String, ?> env) {
		if (env == null) return null;
		final Object value = env.get(getKey());
		return getType().cast(value);
	}

	public IOptional<T> getOptional(Map<String, ?> env) {
		if ((env == null) || !env.containsKey(getKey())) return NullableOptional.empty();
		final Object value = env.get(getKey());
		return NullableOptional.of(getType().cast(value));
	}

	public void put(final Map<String, ? super T> map, T value) {
		if ((value != null) && !getType().isInstance(value)) throw new IllegalArgumentException();
		map.put(getKey(), value);
	}
}
