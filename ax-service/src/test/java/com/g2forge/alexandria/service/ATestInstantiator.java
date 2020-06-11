package com.g2forge.alexandria.service;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.function.type.ITypeFunction1;

public abstract class ATestInstantiator {
	public interface IInterface {}

	public static class NormalClass implements IInterface {}

	public static class SingletonClass implements IInterface, ISingleton {
		protected static final SingletonClass INSTANCE = new SingletonClass();

		public static SingletonClass create() {
			return INSTANCE;
		}

		private SingletonClass() {}
	}
	
	protected abstract ITypeFunction1<IInterface> getInstantiator();
}
