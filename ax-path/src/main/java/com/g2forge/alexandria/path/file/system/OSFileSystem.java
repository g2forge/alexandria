package com.g2forge.alexandria.path.file.system;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;

public enum OSFileSystem implements IStandardFileSystem {
	Microsoft,
	POSIX;

	public static OSFileSystem getDirectorySystem() {
		final PlatformCategory category = HPlatform.getPlatform().getCategory();
		switch (category) {
			case Microsoft:
				return OSFileSystem.Microsoft;
			case Posix:
				return OSFileSystem.POSIX;
			default:
				throw new EnumException(PlatformCategory.class, category);
		}
	}
}
