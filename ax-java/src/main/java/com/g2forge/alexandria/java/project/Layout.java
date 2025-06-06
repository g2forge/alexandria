package com.g2forge.alexandria.java.project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An enumeration of all the known project layouts. Clients of this enum must be prepared for additional values to be added in future releases.
 */
public enum Layout {
	/** The class was loaded from a locally compiled non-JAR maven main source tree. */
	MavenMain {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path root = path.resolve("../..");
			final Path main = root.resolve("src/main");
			return new Location(this, new Project(root), main.resolve("java"), main.resolve("resources"), path);
		}

		@Override
		public boolean isApplicableToTarget(Path path) {
			final int nameCount = path.getNameCount();
			if (nameCount < 2) return false;
			if (!path.getName(nameCount - 1).toString().equals("classes")) return false;
			if (!path.getName(nameCount - 2).toString().equals("target")) return false;
			return true;
		}
	},
	/** The class was loaded from a locally compiled non-JAR maven test source tree. */
	MavenTest {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path root = path.resolve("../..");
			final Path main = root.resolve("src/test");
			return new Location(this, new Project(root), main.resolve("java"), main.resolve("resources"), path);
		}

		@Override
		public boolean isApplicableToTarget(Path path) {
			final int nameCount = path.getNameCount();
			if (nameCount < 2) return false;
			if (!path.getName(nameCount - 1).toString().equals("test-classes")) return false;
			if (!path.getName(nameCount - 2).toString().equals("target")) return false;
			return true;
		}
	},
	/** The class was loaded from a locally compiled maven generated JAR file. */
	MavenJar {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path jar = Paths.get(path.getFileSystem().toString());
			final Path target = jar.getParent();
			return new Location(this, new Project(target.getParent()), null, null, target);
		}

		@Override
		public boolean isApplicableToTarget(Path path) {
			final int nameCount = path.getNameCount();
			if (nameCount != 0) return false;

			final String filename = path.getFileSystem().toString();
			if (!filename.toLowerCase().endsWith("." + HProject.JAR)) return false;
			final Path jar = Paths.get(filename);
			if (!Files.isRegularFile(jar)) return false;
			if (!Files.isRegularFile(jar.getParent().getParent().resolve(HProject.POM))) return false;
			return true;
		}
	},
	Jar {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path jar = Paths.get(path.getFileSystem().toString());
			return new Location(this, new Project(jar), null, null, jar);
		}

		@Override
		public boolean isApplicableToTarget(Path path) {
			final int nameCount = path.getNameCount();
			if (nameCount != 0) return false;

			final String filename = path.getFileSystem().toString();
			if (!filename.toLowerCase().endsWith("." + HProject.JAR)) return false;
			final Path jar = Paths.get(filename);
			if (!Files.isRegularFile(jar)) return false;
			if (Files.isRegularFile(jar.getParent().getParent().resolve(HProject.POM))) return false;
			return true;
		}
	};

	public abstract Location createFromTarget(Path path);

	public abstract boolean isApplicableToTarget(Path path);
}