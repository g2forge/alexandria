package com.g2forge.alexandria.test;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;

import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAssume extends Assume {
	public static <T> T assumeNoException(ISupplier<? extends T> supplier) {
		try {
			return supplier.get();
		} catch (Throwable throwable) {
			throw new AssumptionViolatedException("Supplier failed!", throwable);
		}
	}
}
