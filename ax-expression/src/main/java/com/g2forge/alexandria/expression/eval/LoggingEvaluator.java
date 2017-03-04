package com.g2forge.alexandria.expression.eval;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.g2forge.alexandria.expression.ExpressionNotEvaluableException;
import com.g2forge.alexandria.expression.IEnvironment;
import com.g2forge.alexandria.expression.IExpression;
import com.g2forge.alexandria.expression.IVariable;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.resource.FlagResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingEvaluator<V extends IVariable<V, N>, N extends IEnvironment<V, N, E>, E extends IExpression<V, N>> implements IEvaluator<V, N, E> {
	protected final FlagResource debug = new FlagResource();

	protected int nesting = 0;

	@Override
	public E apply(E expression, N environment, Supplier<E> supplier) {
		return log(getHeaderApply(null, expression, environment), supplier);
	}

	@Override
	public E apply(String description, E expression, N environment) {
		return log(description, getHeaderApply(description, expression, environment), () -> expression.apply(environment));
	}

	@Override
	public ICloseable debug() {
		return debug.open(true);
	}

	@Override
	public <L> L eval(E expression, Class<L> type, Supplier<L> supplier) throws ExpressionNotEvaluableException {
		return log(getHeaderEval(null, expression), supplier);
	}

	protected Consumer<String> getHeaderApply(String description, E expression, N environment) {
		return prefix -> {
			if (description != null) log.debug("{}Apply {}", prefix, description);
			else log.debug("{}Apply", prefix);
			log.debug("{}  {}", prefix, environment);
			log.debug("{}  {}", prefix, expression);
		};
	}

	protected Consumer<String> getHeaderEval(String description, E expression) {
		return prefix -> {
			if (description != null) log.debug("{}Eval {} {}", prefix, description, expression);
			else log.debug("{}Eval {}", prefix, expression);
		};
	}

	protected Consumer<String> getHeaderReduce(String description, E expression) {
		return prefix -> {
			if (description != null) log.debug("{}Reduce {} {}", prefix, description, expression);
			else log.debug("{}Reduce {}", prefix, expression);
		};
	}

	protected String getPrefix() {
		if (nesting == 0) return "";
		return new String(new char[nesting * 2]).replace('\0', ' ');
	}

	public <T> T log(Consumer<String> header, Supplier<T> supplier) {
		if (debug.get() || (nesting > 0)) return supplier.get();

		header.accept("");
		nesting++;
		T retVal = null;
		try {
			retVal = supplier.get();
		} finally {
			nesting--;
			log.debug("->{}", retVal);
		}
		return retVal;
	}

	public <T> T log(String description, Consumer<String> header, Supplier<?> supplier) {
		if (debug.get()) {
			@SuppressWarnings("unchecked")
			final T retVal = (T) supplier.get();
			return retVal;
		}

		final String prefix = getPrefix();
		header.accept(prefix);
		nesting++;
		T retVal = null;
		try {
			@SuppressWarnings("unchecked")
			final T cast = (T) supplier.get();
			retVal = cast;
		} finally {
			nesting--;
			log.debug("{}->{}", prefix, retVal);
		}
		return retVal;
	}

	@Override
	public E reduce(E expression, Supplier<E> supplier) {
		return log(getHeaderReduce(null, expression), supplier);
	}

	@Override
	public E reduce(String description, E expression) {
		return log(description, getHeaderReduce(description, expression), () -> expression.reduce());
	}

	@Override
	public <T> T eval(String description, E expression, Class<T> type, IFunction2<? super E, ? super Class<T>, ? extends T> function) throws ExpressionNotEvaluableException {
		return log(description, getHeaderEval(description, expression), () -> function.apply(expression, type));
	}
}
