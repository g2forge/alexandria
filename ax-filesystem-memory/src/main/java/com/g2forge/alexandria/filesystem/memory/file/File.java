package com.g2forge.alexandria.filesystem.memory.file;

import java.util.Arrays;
import java.util.Map;

import com.g2forge.alexandria.filesystem.memory.attributes.BasicAttributes;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class File implements IEntry {
	protected final BasicAttributes basicAttributes;

	protected byte[] data;

	@Override
	public IEntry copy(boolean attributes, ISupplier<Map<String, IEntry>> entriesSupplier) {
		final FileBuilder retVal = File.builder();
		retVal.basicAttributes(getBasicAttributes().copy(attributes));
		final byte[] data = getData();
		retVal.data(Arrays.copyOf(data, data.length));
		return retVal.build();
	}
}
