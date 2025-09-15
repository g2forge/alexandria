package com.g2forge.alexandria.path.path.filename;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HPath;
import com.g2forge.alexandria.path.path.IPath;

public class Filename extends com.g2forge.alexandria.path.path.Path<String> {
	public static Path addExtension(Path path, String extension) {
		final IPath<String> filename = Filename.fromPath(path).resolve(new com.g2forge.alexandria.path.path.Path<>(extension));
		return HPath.replaceFilename(path, filename.toString());
	}

	public static Filename fromPath(Path path) {
		return FilenamePathFormat.create().toPath(path.getFileName().toString());
	}

	public static Filename fromString(String string) {
		return FilenamePathFormat.create().toPath(string);
	}

	/**
	 * Get the file extension.
	 * 
	 * @param path The path to the file to get the extension of.
	 * @return The extension, including the period.
	 */
	public static String getExtension(Path path) {
		return Filename.fromPath(path).getLast();
	}

	public static Path insertExtension(Path path, String extension) {
		final Filename filename = fromPath(path);
		final List<String> oldComponents = HCollection.asList(filename.getComponents().toCollection());
		final int size = oldComponents.size();
		if (size < 2) throw new IllegalArgumentException(path + " has only one file name component, cannot insert an extension!");
		final IPath<String> newFilename = filename.getPrefix().resolve(fromString(extension)).resolve(fromString(filename.getLast()));
		return HPath.replaceFilename(path, newFilename.toString());
	}

	public static Path removeExtension(Path path, String extension) {
		final Filename withExtension = Filename.fromPath(path);
		final IPath<String> withoutExtension = withExtension.subPath(0, withExtension.size() - new com.g2forge.alexandria.path.path.Path<>(extension).size());
		return HPath.replaceFilename(path, withoutExtension.toString());
	}

	public static Path replaceExtension(Path path, String extension) {
		final IPath<String> filename = Filename.fromPath(path).resolveSibling(new com.g2forge.alexandria.path.path.Path<>(extension));
		return HPath.replaceFilename(path, filename.toString());
	}

	public Filename(Collection<String> components) {
		super(components);
	}

	public Filename(ICollection<String> components) {
		super(components);
	}

	public Filename(String... components) {
		super(components);
	}

	@Override
	protected Filename cast(IPath<String> path) {
		if (path instanceof Filename) return (Filename) path;
		return create(path.getComponents().toCollection());
	}

	@Override
	protected Filename create(Collection<String> components) {
		return new Filename(components);
	}

	/**
	 * Get the last extension on the filename.
	 * 
	 * @return The last extension on the filename.
	 * @see #getPrefix()
	 */
	public String getExtension() {
		if (size() > 1) return getLast();
		return null;
	}

	/**
	 * Get the name of the file, without any extensions.
	 * 
	 * @return The name of the file without any extensions.
	 * @see #getSuffix()
	 */
	public String getName() {
		if (isEmpty()) return null;
		return getFirst();
	}

	/**
	 * Get the name of the file without the last extension.
	 * 
	 * @return The name of the file without the last extension.
	 * @see #getExtension()
	 */
	public Filename getPrefix() {
		final List<String> list = HCollection.asList(components.toCollection());
		return create(list.subList(0, list.size() == 1 ? 1 : list.size() - 1));
	}

	/**
	 * Get all the extensions on the file without the name.
	 * 
	 * @return All the extensions on the file without the name.
	 * @see #getName()
	 */
	public Filename getSuffix() {
		if (size() <= 1) return null;
		final List<String> list = HCollection.asList(components.toCollection());
		return create(list.subList(1, list.size()));
	}

	@Override
	public String toString() {
		return FilenamePathFormat.create().toString(this);
	}
}