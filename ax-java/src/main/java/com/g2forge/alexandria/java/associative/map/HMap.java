package com.g2forge.alexandria.java.associative.map;

import java.util.Map;
import java.util.function.Function;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMap {
	public static <K, V> V createOrGet(Map<K, V> map, K key, Function<? super K, ? extends V> function) {
		if (map.containsKey(key)) return map.get(key);

		final V retVal = function.apply(key);
		map.put(key, retVal);
		return retVal;
	}
}
