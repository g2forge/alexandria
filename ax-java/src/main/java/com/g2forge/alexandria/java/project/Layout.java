package com.g2forge.alexandria.java.project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Layout {
	MavenMain {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path root = path.resolve("../..");
			final Path main = root.resolve("src/main");
			return new Location(new Project(root), main.resolve("java"), main.resolve("resources"), path);
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
	MavenTest {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path root = path.resolve("../..");
			final Path main = root.resolve("src/test");
			return new Location(new Project(root), main.resolve("java"), main.resolve("resources"), path);
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
	MavenJar {
		@Override
		public Location createFromTarget(Path path) {
			if (!isApplicableToTarget(path)) return null;
			final Path jar = Paths.get(path.getFileSystem().toString());
			final Path target = jar.getParent();
			return new Location(new Project(target.getParent()), null, null, target);
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
	};

	public abstract Location createFromTarget(Path path);

	public abstract boolean isApplicableToTarget(Path path);
}