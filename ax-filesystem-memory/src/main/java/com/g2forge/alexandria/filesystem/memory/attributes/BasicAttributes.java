package com.g2forge.alexandria.filesystem.memory.attributes;

import java.nio.file.attribute.FileTime;
import java.time.Instant;

import com.g2forge.alexandria.filesystem.attributes.IGenericBasicAttributeModifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BasicAttributes implements IGenericBasicAttributeModifier {
	public static BasicAttributes createNow() {
		final FileTime time = FileTime.from(Instant.now());
		return new BasicAttributes(time, time, time);
	}

	protected static FileTime optionalTime(FileTime time) {
		return time == null ? FileTime.from(Instant.now()) : time;
	}

	protected FileTime createTime;

	protected FileTime modifyTime;

	protected FileTime accessTime;

	public void access(FileTime time) {
		setAccessTime(optionalTime(time));
	}

	public BasicAttributes copy(boolean attributes) {
		return new BasicAttributes(attributes ? getCreateTime() : FileTime.from(Instant.now()), getModifyTime(), getAccessTime());
	}

	public void modify(FileTime time) {
		final FileTime optionalTime = optionalTime(time);
		setAccessTime(optionalTime);
		setModifyTime(optionalTime);
	}
}
