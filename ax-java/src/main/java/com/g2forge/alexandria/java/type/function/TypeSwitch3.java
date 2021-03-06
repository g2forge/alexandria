package com.g2forge.alexandria.java.type.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IConsumer3;
import com.g2forge.alexandria.java.function.IFunction3;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch3<I0, I1, I2, O> implements IFunction3<I0, I1, I2, O> {
	public static class ConsumerBuilder<I0, I1, I2> {
		protected final Collection<TypedFunction3<?, ?, ?, Void>> functions = new ArrayList<>();

		protected IConsumer3<? super I0, ? super I1, ? super I2> fallback = null;

		public <T0, T1, T2> ConsumerBuilder<I0, I1, I2> add(Class<T0> type0, Class<T1> type1, Class<T2> type2, IConsumer3<? super T0, ? super T1, ? super T2> consumer) {
			functions.add(new TypedFunction3<T0, T1, T2, Void>(type0, type1, type2, (i0, i1, i2) -> {
				consumer.accept(i0, i1, i2);
				return null;
			}));
			return this;
		}

		public IConsumer3<I0, I1, I2> build() {
			if (functions.isEmpty()) return IConsumer3.ignore();
			final TypeSwitch3<I0, I1, I2, Void> ts = new TypeSwitch3<>(fallback == null ? null : (i0, i1, i2) -> {
				fallback.accept(i0, i1, i2);
				return null;
			}, functions);
			return (i0, i1, i2) -> ts.apply(i0, i1, i2);
		}

		public ConsumerBuilder<I0, I1, I2> fallback(IConsumer3<? super I0, ? super I1, ? super I2> consumer) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback consumer!");
			this.fallback = consumer;
			return this;
		}

		public ConsumerBuilder<I0, I1, I2> with(IConsumer1<? super ConsumerBuilder<I0, I1, I2>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	public static class FunctionBuilder<I0, I1, I2, O> {
		protected final Collection<TypedFunction3<?, ?, ?, O>> functions = new ArrayList<>();

		protected IFunction3<? super I0, ? super I1, ? super I2, ? extends O> fallback = null;

		public <T0, T1, T2> FunctionBuilder<I0, I1, I2, O> add(Class<T0> type0, Class<T1> type1, Class<T2> type2, IConsumer3<? super T0, ? super T1, ? super T2> consumer, O output) {
			add(type0, type1, type2, (i0, i1, i2) -> {
				consumer.accept(i0, i1, i2);
				return output;
			});
			return this;
		}

		public <T0, T1, T2> FunctionBuilder<I0, I1, I2, O> add(Class<T0> type0, Class<T1> type1, Class<T2> type2, IFunction3<? super T0, ? super T1, ? super T2, ? extends O> function) {
			functions.add(new TypedFunction3<T0, T1, T2, O>(type0, type1, type2, function));
			return this;
		}

		public IFunction3<I0, I1, I2, O> build() {
			if (functions.isEmpty()) return (i0, i1, i2) -> fallback.apply(i0, i1, i2);
			return new TypeSwitch3<>(fallback, functions);
		}

		public FunctionBuilder<I0, I1, I2, O> fallback(IFunction3<? super I0, ? super I1, ? super I2, ? extends O> function) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback function!");
			this.fallback = function;
			return this;
		}

		public FunctionBuilder<I0, I1, I2, O> with(IConsumer1<? super FunctionBuilder<I0, I1, I2, O>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	@RequiredArgsConstructor
	@ToString(callSuper = true)
	@Getter
	protected static class Node<O> extends ANode<O, Node<O>> {
		protected final ITypedFunction3<?, ?, ?, ? extends O> function;

		protected <I0, I1, I2> O apply(IFunction3<? super I0, ? super I1, ? super I2, ? extends O> fallback, I0 input0, I1 input1, I2 input2) {
			return get(n -> n.getFunction().isApplicable(input0, input1, input2), collection -> {
				if (collection.isEmpty()) {
					if (fallback == null) throw new IllegalArgumentException(String.format("There was no fallback, and no case was found for: %1$s, %2$s, %3$s", input0, input1, input2));
					return new Node<O>(new TypedFunction3<>(null, null, null, fallback));
				}
				try {
					return HCollection.getOne(collection);
				} catch (IllegalArgumentException exception) {
					throw new IllegalArgumentException(String.format("Multiple cases were found for: %1$s, %2$s, %3$s", input0, input1, input2), exception);
				}
			}).getFunction().apply(input0, input1, input2);
		}

		protected boolean isAncestor(Node<O> node) {
			final ITypedFunction3<?, ?, ?, ? extends O> thisFunction = getFunction();
			if (thisFunction == null) return true;
			if (!thisFunction.getInput0Type().isAssignableFrom(node.getFunction().getInput0Type())) return false;
			if (!thisFunction.getInput1Type().isAssignableFrom(node.getFunction().getInput1Type())) return false;
			if (!thisFunction.getInput2Type().isAssignableFrom(node.getFunction().getInput2Type())) return false;
			return true;
		}

		protected boolean isDescendant(Node<O> node) {
			final ITypedFunction3<?, ?, ?, ? extends O> thisFunction = getFunction();
			if (thisFunction == null) return false;
			if (!node.getFunction().getInput0Type().isAssignableFrom(thisFunction.getInput0Type())) return false;
			if (!node.getFunction().getInput1Type().isAssignableFrom(thisFunction.getInput1Type())) return false;
			if (!node.getFunction().getInput2Type().isAssignableFrom(thisFunction.getInput2Type())) return false;
			return true;
		}

		@Override
		protected boolean isObjectRoot() {
			return getFunction() == null;
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final IFunction3<? super I0, ? super I1, ? super I2, ? extends O> fallback;

	@Getter(AccessLevel.PROTECTED)
	protected final Node<O> root;

	public TypeSwitch3(IFunction3<? super I0, ? super I1, ? super I2, ? extends O> fallback, Collection<? extends ITypedFunction3<?, ?, ?, ? extends O>> functions) {
		this.fallback = fallback;
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch3(IFunction3<? super I0, ? super I1, ? super I2, ? extends O> fallback, TypedFunction3<I0, I1, I2, O>... functions) {
		this(fallback, Arrays.asList(functions));
	}

	@Override
	public O apply(I0 input0, I1 input1, I2 input2) {
		return getRoot().apply(getFallback(), input0, input1, input2);
	}
}
