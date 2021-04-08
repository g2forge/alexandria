package com.g2forge.alexandria.java.io.file.compare;

import java.nio.file.Path;
import java.util.Collection;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.error.OrThrowable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class HashFileCompareGroup implements IFileCompareGroup {
	protected final OrThrowable<String> value;

	@Override
	public String describe(Collection<Path> roots, Path relative) {
		if (value.isEmpty()) return HError.toString(value.getThrowable());
		return value.get();
	}
}