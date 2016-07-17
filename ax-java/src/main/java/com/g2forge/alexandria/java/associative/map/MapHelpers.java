package com.g2forge.alexandria.java.associative.map;

import java.util.Map;
import java.util.function.Function;

public class MapHelpers {
	public static <K, V> V createOrGet(Map<K, V> map, K key, Function<? super K, ? extends V> function) {
		if (map.containsKey(key)) return map.get(key);

		final V retVal = function.apply(key);
		map.put(key, retVal);
		return retVal;
	}
}
