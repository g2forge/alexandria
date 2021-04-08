package com.g2forge.alexandria.java.io.file.compare;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.core.helpers.HBinary;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.io.HIO;

public class SHA1HashFileCompareGroupFunction extends AHashFileCompareGroupFunction implements ISingleton {
	protected static final SHA1HashFileCompareGroupFunction INSTANCE = new SHA1HashFileCompareGroupFunction();

	public static SHA1HashFileCompareGroupFunction create() {
		return INSTANCE;
	}

	@Override
	protected String computeHashString(Path path) throws IOException {
		return "L" + Files.size(path) + "H" + HBinary.toHex(HIO.sha1(path, Files::newInputStream));
	}
}