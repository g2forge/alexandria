package com.g2forge.alexandria.path.directory;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.platform.HPlatform;
import com.g2forge.alexandria.java.platform.PlatformCategory;

public enum DirectorySystem implements IStandardDirectorySystem {
	Microsoft,
	POSIX;

	public static DirectorySystem getDirectorySystem() {
		final PlatformCategory category = HPlatform.getPlatform().getCategory();
		switch (category) {
			case Microsoft:
				return DirectorySystem.Microsoft;
			case Posix:
				return DirectorySystem.POSIX;
			default:
				throw new EnumException(PlatformCategory.class, category);
		}
	}
}
