package com.g2forge.alexandria.wizard;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Properties;

import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PropertyStringInput extends AInput<String> {
	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final Properties properties = computeProperties();

	protected static Properties computeProperties() {
		final String path = System.getProperty(PropertyStringInput.class.getPackage().getName() + ".propertiesfile");
		if (path == null) return System.getProperties();

		try (InputStream input = Files.newInputStream(Paths.get(path))) {
			final Properties prop = new Properties();
			prop.load(input);
			return prop;
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Getter
	protected final String property;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final String value = getProperties().getProperty(getProperty());

	@Override
	public String get() {
		if (isEmpty()) { throw new NoSuchElementException(String.format("The property \"%1$s\" was not set!", getProperty())); }
		return getValue();
	}

	@Override
	public boolean isEmpty() {
		return getValue() == null;
	}
}
