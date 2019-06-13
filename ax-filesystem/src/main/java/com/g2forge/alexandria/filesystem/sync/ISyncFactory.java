package com.g2forge.alexandria.filesystem.sync;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IThrowConsumer1;
import com.g2forge.alexandria.java.function.IThrowFunction1;

public interface ISyncFactory {
	/**
	 * Get the appropriate sync function, which will wrap an instance of {@link IFunction1} in a lock specified by the given object. It may also provider other
	 * functionality in the wrapper such as allocating and freeing resources.
	 * 
	 * @param <P> The path type
	 * @param <O> The output type
	 * @return A function which can wrap a functional to provide synchronization.
	 */
	public <P, O> IFunction2<? super IFunction1<P, O>, ? super Object, ? extends IFunction1<P, O>> getSyncFunction1();

	/**
	 * Get the appropriate sync function, which will wrap an instance of {@link IThrowConsumer1} in a lock specified by the given object. It may also provider
	 * other functionality in the wrapper such as allocating and freeing resources.
	 * 
	 * @param <P> The path type
	 * @param <T> The throwable type
	 * @return A function which can wrap a functional to provide synchronization.
	 */
	public <P, T extends Throwable> IFunction2<? super IThrowConsumer1<P, T>, ? super Object, ? extends IThrowConsumer1<P, T>> getSyncThrowConsumer1();

	/**
	 * Get the appropriate sync function, which will wrap an instance of {@link IThrowConsumer1} in a lock specified by the given object. It may also provider
	 * other functionality in the wrapper such as allocating and freeing resources.
	 * 
	 * @param <P> The path type
	 * @param <T> The throwable type
	 * @return A function which can wrap a functional to provide synchronization.
	 */
	public <P, T extends Throwable> IFunction2<? super IThrowConsumer1<P[], T>, ? super Object, ? extends IThrowConsumer1<P[], T>> getSyncThrowConsumerN();

	/**
	 * Get the appropriate sync function, which will wrap an instance of {@link IThrowFunction1} in a lock specified by the given object. It may also provider
	 * other functionality in the wrapper such as allocating and freeing resources.
	 * 
	 * @param <P> The path type
	 * @param <O> The output type
	 * @param <T> The throwable type
	 * @return A function which can wrap a functional to provide synchronization.
	 */
	public <P, O, T extends Throwable> IFunction2<? super IThrowFunction1<P, O, T>, ? super Object, ? extends IThrowFunction1<P, O, T>> getSyncThrowFunction1();

	/**
	 * Get the appropriate sync function, which will wrap an instance of {@link IThrowFunction1} in a lock specified by the given object. It may also provider
	 * other functionality in the wrapper such as allocating and freeing resources.
	 * 
	 * @param <P> The path type
	 * @param <O> The output type
	 * @param <T> The throwable type
	 * @return A function which can wrap a functional to provide synchronization.
	 */
	public <P, O, T extends Throwable> IFunction2<? super IThrowFunction1<P[], O, T>, ? super Object, ? extends IThrowFunction1<P[], O, T>> getSyncThrowFunctionN();
}
