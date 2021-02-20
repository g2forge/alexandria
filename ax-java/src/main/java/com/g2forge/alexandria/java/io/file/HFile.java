package com.g2forge.alexandria.java.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HFile {
	public static void copy(Path source, Path target, CopyOption... options) {
		CopyWalker.builder().target(target).options(IFunction1.create(options)).build().walkFileTree(source);
	}

	public static void delete(Path path, boolean onexit) {
		DeleteWalker.builder().onexit(onexit).build().walkFileTree(path);
	}

	public static void gc() {
		gc(3, 100);
	}

	public static void gc(int repeat, int pause) {
		for (int i = 0; i < repeat; i++) {
			if (i > 0) HConcurrent.wait(HFile.class, pause);
			System.gc();
		}
	}

	public static List<Path> toList(DirectoryStream<Path> stream) {
		try {
			final List<Path> retVal = new ArrayList<>();
			for (Path path : stream) {
				retVal.add(path);
			}
			return retVal;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}
	}
}
