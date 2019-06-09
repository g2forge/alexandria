package com.g2forge.alexandria.filesystem.attributes;

import java.nio.file.attribute.FileTime;

/**
 * A helper interface to allow {@link com.g2forge.alexandria.filesystem.AGenericFileSystemProvider} to update file times, rather than requiring the implementor
 * of the filesystem to do so manually.
 */
public interface IGenericBasicAttributeModifier {
	/**
	 * Mark this file as having been accessed at the specified time.
	 * 
	 * @param time Optional access time. Can be <code>null</code> to indicate "now".
	 */
	public void access(FileTime time);

	public FileTime getAccessTime();

	public FileTime getCreateTime();

	public FileTime getModifyTime();

	/**
	 * Mark this file as having been modified at the specified time. Note that this should also update the access time and any other attributes, if those are
	 * the appropriate semantics for this file system.
	 * 
	 * @param time Optional modification time. Can be <code>null</code> to indicate "now".
	 */
	public void modify(FileTime time);
}
