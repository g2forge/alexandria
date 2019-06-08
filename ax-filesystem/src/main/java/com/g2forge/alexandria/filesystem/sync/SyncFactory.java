package com.g2forge.alexandria.filesystem.sync;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IThrowConsumer1;
import com.g2forge.alexandria.java.function.IThrowFunction1;

public class SyncFactory implements ISyncFactory, ISingleton {
	protected static final SyncFactory INSTANCE = new SyncFactory();

	public static SyncFactory create() {
		return INSTANCE;
	}

	@Override
	public <P, O> IFunction2<? super IFunction1<P, O>, ? super Object, ? extends IFunction1<P, O>> getSyncFunction1() {
		return IFunction1::sync;
	}

	@Override
	public <P, T extends Throwable> IFunction2<? super IThrowConsumer1<P, T>, ? super Object, ? extends IThrowConsumer1<P, T>> getSyncThrowConsumer1() {
		return IThrowConsumer1::sync;
	}

	@Override
	public <P, T extends Throwable> IFunction2<? super IThrowConsumer1<P[], T>, ? super Object, ? extends IThrowConsumer1<P[], T>> getSyncThrowConsumerN() {
		return IThrowConsumer1::sync;
	}

	@Override
	public <P, O, T extends Throwable> IFunction2<? super IThrowFunction1<P, O, T>, ? super Object, ? extends IThrowFunction1<P, O, T>> getSyncThrowFunction1() {
		return IThrowFunction1::sync;
	}

	@Override
	public <P, O, T extends Throwable> IFunction2<? super IThrowFunction1<P[], O, T>, ? super Object, ? extends IThrowFunction1<P[], O, T>> getSyncThrowFunctionN() {
		return IThrowFunction1::sync;
	}
}
