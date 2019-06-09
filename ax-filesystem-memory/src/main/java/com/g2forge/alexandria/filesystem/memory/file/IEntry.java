package com.g2forge.alexandria.filesystem.memory.file;

import java.util.Map;

import com.g2forge.alexandria.filesystem.memory.attributes.BasicAttributes;
import com.g2forge.alexandria.java.function.ISupplier;

public interface IEntry {
	public BasicAttributes getBasicAttributes();
	
	public IEntry copy(boolean attributes, ISupplier<Map<String, IEntry>> entriesSupplier);
}
