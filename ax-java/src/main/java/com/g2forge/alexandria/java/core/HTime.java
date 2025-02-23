package com.g2forge.alexandria.java.core;

import java.time.Instant;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HTime {
	public static Instant min(Instant i0, Instant i1) {
		return i1.isBefore(i0) ? i1 : i0;
	}

	public static Instant max(Instant i0, Instant i1) {
		return i1.isAfter(i0) ? i1 : i0;
	}
}
