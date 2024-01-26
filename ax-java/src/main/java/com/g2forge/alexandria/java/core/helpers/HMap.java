package com.g2forge.alexandria.java.core.helpers;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

import com.g2forge.alexandria.java.core.marker.Helpers;

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

	public static <K, V> Map<K, V> empty() {
		return Collections.emptyMap();
	}

	@SafeVarargs
	public static <K, V> Map<K, V> merge(Map<K, V>... maps) {
		if (maps.length == 1) return maps[0];
		final Map<K, V> retVal = new LinkedHashMap<>();
		for (Map<K, V> map : maps) {
			retVal.putAll(map);
		}
		return retVal;
	}

	public static <T> Map<String, T> copy(Map<String, T> map) {
		if (map instanceof SortedMap) {
			final SortedMap<String, T> cast = (SortedMap<String, T>) map;
			final TreeMap<String, T> retVal = new TreeMap<>(cast.comparator());
			retVal.putAll(map);
			return retVal;
		}
		return new LinkedHashMap<>(map);
	}

	public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
		if (map instanceof SortedMap) return Collections.unmodifiableSortedMap((SortedMap<K, V>) map);
		return Collections.unmodifiableMap(map);
	}
}
