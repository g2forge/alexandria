package com.g2forge.alexandria.filesystem.memory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.g2forge.alexandria.filesystem.ATestFileSystemProvider;

public class TestMemoryFileSystemProvider extends ATestFileSystemProvider {
	protected static final String MYFS = "memory:myfs";

	@After
	public void after() throws IOException {
		fs.close();
		fs = null;
	}

	@Before
	public void before() throws IOException {
		Assert.assertNull(fs);
		fs = FileSystems.newFileSystem(URI.create(MYFS), null);
	}

	protected Path createPath(String absolute) {
		return Paths.get(URI.create(MYFS + "!" + absolute));
	}
}
