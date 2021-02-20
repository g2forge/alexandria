package com.g2forge.alexandria.filesystem.memory.file;

import java.util.Map;

import com.g2forge.alexandria.filesystem.memory.attributes.BasicAttributes;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Directory implements IEntry {
	protected final BasicAttributes basicAttributes;

	protected final Map<String, IEntry> entries;

	@Override
	public IEntry copy(boolean attributes, ISupplier<Map<String, IEntry>> entriesSupplier) {
		final DirectoryBuilder retVal = Directory.builder();
		retVal.basicAttributes(getBasicAttributes().copy(attributes));
		retVal.entries(entriesSupplier.get());
		return retVal.build();
	}
}
