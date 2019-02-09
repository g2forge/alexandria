package com.g2forge.alexandria.filesystem.memory.file;

import com.g2forge.alexandria.filesystem.file.IGenericEntryAccessor;
import com.g2forge.alexandria.filesystem.path.GenericPath;
import com.g2forge.alexandria.filesystem.path.IGenericFileSystemInternal;
import com.g2forge.alexandria.java.core.iface.ISingleton;

public class EntryAccessor implements IGenericEntryAccessor<IEntry, File, Directory, GenericPath>, ISingleton {
	protected static final EntryAccessor INSTANCE = new EntryAccessor();

	public static EntryAccessor create() {
		return INSTANCE;
	}

	@Override
	public Directory asDirectory(IEntry entry) {
		return (Directory) entry;
	}

	@Override
	public IEntry asEntry(Directory directory) {
		return directory;
	}

	@Override
	public File asFile(IEntry entry) {
		return (File) entry;
	}

	@Override
	public IEntry getEntry(Directory directory, String name) {
		return directory.getEntries().get(name);
	}

	@Override
	public IGenericFileSystemInternal<GenericPath> getGenericFileSystemInternal(GenericPath path) {
		return path.getGenericFileSystem();
	}

	@Override
	public boolean isDirectory(IEntry entry) {
		return entry instanceof Directory;
	}

	@Override
	public boolean isFile(IEntry entry) {
		return entry instanceof File;
	}
}