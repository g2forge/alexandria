package com.g2forge.alexandria.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceConfigurationError;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.collection.IIteratorCollection;
import com.g2forge.alexandria.java.function.typed.ITypedFunction1;
import com.g2forge.alexandria.java.function.typed.TypedMapIterator;

import lombok.Getter;

public class BasicServiceLoader<S> implements IServiceLoader<S> {
	public class ProviderIterator implements Iterator<Class<? extends S>> {
		protected final Iterator<Class<? extends S>> loaded = BasicServiceLoader.this.loaded.values().iterator();

		@Override
		public boolean hasNext() {
			if (loaded.hasNext()) return true;
			return loader.hasNext();
		}

		@Override
		public Class<? extends S> next() {
			if (loaded.hasNext()) return loaded.next();
			return loader.next();
		}
	}

	protected class ProviderLoader implements Iterator<Class<? extends S>> {
		protected Enumeration<URL> allConfigs = null;

		protected Iterator<String> currentConfig = null;

		protected String nextProviderName = null;

		@Override
		public boolean hasNext() {
			if (acc == null) return hasNextInternal();
			else return AccessController.doPrivileged((PrivilegedAction<Boolean>) this::hasNextInternal, acc);
		}

		protected boolean hasNextInternal() {
			if (nextProviderName != null) return true;
			loadConfigs();
			loadConfig();
			if (currentConfig == null) return false;
			nextProviderName = currentConfig.next();
			return true;
		}

		protected void loadConfig() throws ServiceConfigurationError {
			while ((currentConfig == null) || !currentConfig.hasNext()) {
				if (!allConfigs.hasMoreElements()) {
					currentConfig = null;
					return;
				}
				currentConfig = parse(allConfigs.nextElement());
			}
		}

		protected void loadConfigs() throws SmartServiceConfigurationError {
			if (allConfigs != null) return;
			try {
				final String resourceName = PREFIX + key.getName();
				if (classLoader == null) allConfigs = ClassLoader.getSystemResources(resourceName);
				else allConfigs = classLoader.getResources(resourceName);
			} catch (IOException exception) {
				throw new SmartServiceConfigurationError(key, "Error locating configuration files", exception);
			}
		}

		@Override
		public Class<? extends S> next() {
			if (acc == null) return nextInternal();
			else return AccessController.doPrivileged((PrivilegedAction<Class<? extends S>>) this::nextInternal, acc);
		}

		protected Class<? extends S> nextInternal() {
			if (!hasNextInternal()) throw new NoSuchElementException();

			final String nextProviderName = this.nextProviderName;
			this.nextProviderName = null;
			final Class<?> nextProviderClass;
			try {
				nextProviderClass = Class.forName(nextProviderName, false, classLoader);
			} catch (ClassNotFoundException exception) {
				throw new SmartServiceConfigurationError(key, "Provider \"" + nextProviderName + "\" not found!", exception);
			}

			if (!type.isAssignableFrom(nextProviderClass)) throw new SmartServiceConfigurationError(key, "Provider \"" + nextProviderName + "\" not a subtype of \"" + type + "\"!");
			@SuppressWarnings("unchecked")
			final Class<? extends S> retVal = (Class<? extends S>) nextProviderClass;
			loaded.put(nextProviderName, retVal);
			return retVal;
		}
	}

	protected static final String PREFIX = "META-INF/services/";

	protected static String readLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line != null) {
			final int comment = line.indexOf('#');
			if (comment >= 0) line = line.substring(0, comment);
			line = line.trim();
		}
		return line;
	}

	@Getter
	protected final Class<?> key;

	@Getter
	protected final Class<S> type;

	protected final ITypedFunction1<S> instantiator;

	protected final ClassLoader classLoader;

	protected final AccessControlContext acc;

	protected final Map<String, Class<? extends S>> loaded = new LinkedHashMap<>();

	protected final ProviderLoader loader;

	public BasicServiceLoader(Class<?> key, Class<S> type) {
		this(key, type, null, null);
	}

	public BasicServiceLoader(Class<?> key, Class<S> type, ClassLoader classLoader, ITypedFunction1<S> instantiator) {
		this.key = key == null ? type : key;
		this.type = Objects.requireNonNull(type, "Type cannot be null");
		this.instantiator = instantiator == null ? new DefaultInstantiator<>(this) : instantiator;

		this.classLoader = (classLoader == null) ? Thread.currentThread().getContextClassLoader() : classLoader;
		this.acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
		loaded.clear();
		loader = new ProviderLoader();
	}

	public BasicServiceLoader(Class<S> type) {
		this(type, type);
	}

	@Override
	public ICollection<? extends Class<? extends S>> find() {
		return ((IIteratorCollection<? extends Class<? extends S>>) ProviderIterator::new);
	}

	@Override
	public ICollection<? extends S> load() {
		return ((IIteratorCollection<? extends S>) () -> new TypedMapIterator<>(find().iterator(), instantiator));
	}

	protected Iterator<String> parse(URL url) throws ServiceConfigurationError {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"))) {
			final List<String> retVal = new ArrayList<>();
			int lineNumber = 1;
			while (true) {
				try {
					final String line = readLine(reader);
					if (line == null) break;
					final int length = line.length();
					if (length <= 0) continue;

					if ((line.indexOf(' ') >= 0) || (line.indexOf('\t') >= 0)) throw new Error("Illegal configuration-file syntax, lines may not contain spaces or tabs");

					int cp = line.codePointAt(0);
					if (!Character.isJavaIdentifierStart(cp)) throw new Error("Illegal class name: " + line);
					for (int i = Character.charCount(cp); i < length; i += Character.charCount(cp)) {
						cp = line.codePointAt(i);
						if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) throw new Error("Illegal class name: " + line);
					}
					retVal.add(line);
				} catch (Throwable throwable) {
					throw new SmartServiceConfigurationError(key, "Failed to parse service configuration file " + url + ":" + lineNumber);
				}
				lineNumber++;
			}
			return retVal.iterator();
		} catch (IOException exception) {
			throw new SmartServiceConfigurationError(key, "Error reading service configuration file", exception);
		}
	}
}