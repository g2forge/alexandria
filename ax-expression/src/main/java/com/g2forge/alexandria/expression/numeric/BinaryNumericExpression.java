package com.g2forge.alexandria.expression.numeric;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.expression.eval.IEvaluator;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HStream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class BinaryNumericExpression extends AActiveNumericExpression {
	@RequiredArgsConstructor
	@Getter
	public enum Associativity {
		Left(true),
		Right(true),
		None(false);

		protected final boolean associative;
	}

	@RequiredArgsConstructor
	@Getter
	public enum Operator {
		Add(Associativity.Left, null, true) {
			@Override
			public NumericLiteral literal(List<NumericLiteral> arguments) {
				if (arguments.isEmpty()) return new NumericLiteral(0);
				if (arguments.size() == 1) HCollection.getOne(arguments);
				int current = 0;
				for (NumericLiteral literal : arguments) {
					current += literal.getValue();
				}
				return new NumericLiteral(current);
			}
		},
		Subtract(Associativity.Left, Add, false) {
			@Override
			public NumericLiteral literal(List<NumericLiteral> arguments) {
				if (arguments.isEmpty()) return new NumericLiteral(0);
				if (arguments.size() == 1) HCollection.getOne(arguments);
				int current = 0;
				boolean first = true;
				for (NumericLiteral literal : arguments) {
					if (first) {
						current += literal.getValue();
						first = false;
					} else current -= literal.getValue();
				}
				return new NumericLiteral(current);
			}
		},
		Mod(Associativity.Left, null, false) {
			@Override
			public NumericLiteral literal(List<NumericLiteral> arguments) {
				if (arguments.isEmpty()) return new NumericLiteral(0);
				if (arguments.size() == 1) HCollection.getOne(arguments);
				int current = 0;
				boolean first = true;
				for (NumericLiteral literal : arguments) {
					if (first) {
						current = literal.getValue();
						first = false;
					} else current = current % literal.getValue();
				}
				return new NumericLiteral(current);
			}
		};

		protected final Associativity associativity;

		protected final Operator reassociativeOperator;

		protected final boolean commutative;

		public abstract NumericLiteral literal(List<NumericLiteral> arguments);

		public INumericExpression reduce(List<INumericExpression> reduced) {
			if (reduced.stream().filter(e -> !(e instanceof NumericLiteral)).findAny().isPresent()) {
				if (getAssociativity().isAssociative()) {
					if (isCommutative()) {
						final Map<Boolean, List<INumericExpression>> grouped = reduced.stream().collect(Collectors.groupingBy(a -> a instanceof NumericLiteral));
						final List<INumericExpression> collection = grouped.entrySet().stream().flatMap(e -> {
							if (e.getKey()) {
								@SuppressWarnings({ "rawtypes", "unchecked" })
								final List<NumericLiteral> cast = (List) e.getValue();
								return Stream.of(literal(cast));
							} else return e.getValue().stream();
						}).collect(Collectors.toList());
						if (collection.size() == 1) return HCollection.getOne(collection);
						return new BinaryNumericExpression(this, collection);
					} else if ((getReassociativeOperator() != null) && getReassociativeOperator().getAssociativity().isAssociative() && getReassociativeOperator().isCommutative()) {
						final boolean left = Associativity.Left.equals(getAssociativity());
						final INumericExpression single = reduced.get(left ? 0 : reduced.size() - 1);
						final INumericExpression multi = new BinaryNumericExpression(getReassociativeOperator(), reduced.subList(left ? 1 : 0, left ? reduced.size() : reduced.size() - 1)).reduce();
						return new BinaryNumericExpression(this, left ? Arrays.asList(single, multi) : Arrays.asList(multi, single));
					}
				}
				return new BinaryNumericExpression(this, reduced);
			} else {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				final List<NumericLiteral> cast = (List) reduced;
				return literal(cast);
			}
		}
	}

	protected final Operator operator;

	protected final List<INumericExpression> arguments;

	public BinaryNumericExpression(Operator operator, INumericExpression... arguments) {
		this(operator, Arrays.asList(arguments));
	}

	@Override
	protected INumericExpression applyInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator, NumericEnvironment environment) {
		final List<INumericExpression> applied = HStream.toStreamIndexed(getArguments()).map(t -> evaluator.apply("Argument" + t.get0(), t.get1(), environment)).collect(Collectors.toList());
		return new BinaryNumericExpression(getOperator(), applied);
	}

	@Override
	protected INumericExpression reduceInternal(IEvaluator<NumericVariable, NumericEnvironment, INumericExpression> evaluator) {
		final List<INumericExpression> reduced = HStream.toStreamIndexed(getArguments()).map(t -> evaluator.reduce("Argument" + t.get0(), t.get1())).collect(Collectors.toList());
		return getOperator().reduce(reduced);
	}

	@Override
	public String toString() {
		try (final ICloseable debug = EVAL.get().debug()) {
			return new StringBuilder().append(getOperator()).append(getArguments()).toString();
		}
	}
}
