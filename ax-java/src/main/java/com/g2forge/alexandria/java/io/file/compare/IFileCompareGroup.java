package com.g2forge.alexandria.java.io.file.compare;

import java.nio.file.Path;
import java.util.Collection;

public interface IFileCompareGroup {
	public String describe(Collection<Path> roots, Path relative);
}