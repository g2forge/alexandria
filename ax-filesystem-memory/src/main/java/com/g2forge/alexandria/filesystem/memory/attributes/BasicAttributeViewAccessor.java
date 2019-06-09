package com.g2forge.alexandria.filesystem.memory.attributes;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.filesystem.attributes.accessor.IAttributeViewAccessor;
import com.g2forge.alexandria.filesystem.file.GenericEntryReference;
import com.g2forge.alexandria.filesystem.memory.file.Directory;
import com.g2forge.alexandria.filesystem.memory.file.File;
import com.g2forge.alexandria.filesystem.memory.file.IEntry;
import com.g2forge.alexandria.filesystem.path.GenericPath;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicAttributeViewAccessor implements IAttributeViewAccessor<BasicFileAttributes, BasicFileAttributeView> {
	@Data
	public static class AttributeAccessor {
		protected final IFunction1<GenericEntryReference<IEntry, File, Directory, GenericPath>, Object> getter;
		protected final IConsumer2<GenericEntryReference<IEntry, File, Directory, GenericPath>, Object> setter;
	}

	protected static final Map<String, BasicAttributeViewAccessor.AttributeAccessor> attributes;

	static {
		final Map<String, BasicAttributeViewAccessor.AttributeAccessor> _attributes = new HashMap<>();
		final IFunction1<GenericEntryReference<IEntry, File, Directory, GenericPath>, BasicAttributes> getAttributes = IFunction1.create(GenericEntryReference<IEntry, File, Directory, GenericPath>::getEntry).andThen(IEntry::getBasicAttributes);
		_attributes.put("creationTime", new AttributeAccessor(getAttributes.andThen(BasicAttributes::getCreateTime), IConsumer2.create(BasicAttributes::setCreateTime).lift0(getAttributes).lift1(FileTime.class::cast)));
		_attributes.put("lastAccessTime", new AttributeAccessor(getAttributes.andThen(BasicAttributes::getAccessTime), IConsumer2.create(BasicAttributes::setAccessTime).lift0(getAttributes).lift1(FileTime.class::cast)));
		_attributes.put("lastModifiedTime", new AttributeAccessor(getAttributes.andThen(BasicAttributes::getModifyTime), IConsumer2.create(BasicAttributes::setModifyTime).lift0(getAttributes).lift1(FileTime.class::cast)));
		attributes = _attributes;
	}

	protected final GenericEntryReference<IEntry, File, Directory, GenericPath> reference;

	@Override
	public Object get(String name) throws NoSuchFileException {
		reference.assertExists();
		final BasicAttributeViewAccessor.AttributeAccessor accessor = attributes.get(name);
		return accessor.getGetter().apply(reference);
	}

	@Override
	public BasicFileAttributes getAttributes() throws NoSuchFileException {
		reference.assertExists();
		return new BasicFileAttributes() {
			@Override
			public FileTime creationTime() {
				return reference.getEntry().getBasicAttributes().getCreateTime();
			}

			@Override
			public Object fileKey() {
				return reference.getResolved();
			}

			@Override
			public boolean isDirectory() {
				return (reference.getEntry() instanceof Directory);
			}

			@Override
			public boolean isOther() {
				return false;
			}

			@Override
			public boolean isRegularFile() {
				return (reference.getEntry() instanceof File);
			}

			@Override
			public boolean isSymbolicLink() {
				return false;
			}

			@Override
			public FileTime lastAccessTime() {
				return reference.getEntry().getBasicAttributes().getAccessTime();
			}

			@Override
			public FileTime lastModifiedTime() {
				return reference.getEntry().getBasicAttributes().getModifyTime();
			}

			@Override
			public long size() {
				if (reference.getEntry() instanceof File) return ((File) reference.getEntry()).getData().length;
				if (reference.getEntry() instanceof Directory) return ((Directory) reference.getEntry()).getEntries().size();
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String getName() {
		return HBasicFileAttributes.ATTRIBUTES_NAME;
	}

	@Override
	public Set<String> getNames() {
		return attributes.keySet();
	}

	@Override
	public BasicFileAttributeView getView() {
		return new BasicFileAttributeView() {
			@Override
			public String name() {
				return getName();
			}

			@Override
			public BasicFileAttributes readAttributes() throws IOException {
				return getAttributes();
			}

			@Override
			public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
				final BasicAttributes attributes = reference.getEntry().getBasicAttributes();
				attributes.setModifyTime(lastModifiedTime);
				attributes.setAccessTime(lastAccessTime);
				attributes.setCreateTime(createTime);
			}
		};
	}

	@Override
	public void set(String name, Object value) throws NoSuchFileException {
		reference.assertExists();
		final BasicAttributeViewAccessor.AttributeAccessor accessor = attributes.get(name);
		accessor.getSetter().accept(reference, value);
	}
}