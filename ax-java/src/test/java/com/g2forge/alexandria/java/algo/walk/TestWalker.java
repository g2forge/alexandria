package com.g2forge.alexandria.java.algo.walk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.ISupplier;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1.FunctionBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

public class TestWalker {
	@Data
	@Builder(toBuilder = true)
	public static class A implements INode {
		protected final String name;

		@Singular
		protected final List<INode> children;
	}

	@Data
	@Builder(toBuilder = true)
	public static class B implements INode {
		protected final String name;

		protected final INode left;

		protected final C right;
	}

	@Data
	@Builder(toBuilder = true)
	public static class C implements INode {
		protected final String name;
	}

	@Data
	@Builder(toBuilder = true)
	public static class D implements INode {
		protected final String name;
	}

	/**
	 * A very simple visitor implementation, internally it re-uses the {@link Step} class to record what methods of the {@link IVisitor} interface were called
	 * in what order. All of the methods of this class continue the walk, but in some of the unit tests we have overridden one or more methods to return a
	 * different {@link VisitResult}.
	 */
	protected static class EventVisitor implements IVisitor {
		@Getter
		protected final List<Step<String>> events = new ArrayList<>();

		@Override
		public VisitResult post(A a) {
			events.add(new Step<>(StepType.Post, a.getName()));
			return VisitResult.Continue;
		}

		@Override
		public VisitResult post(B b) {
			events.add(new Step<>(StepType.Post, b.getName()));
			return VisitResult.Continue;
		}

		@Override
		public VisitResult post(INode parent, C c) {
			events.add(new Step<>(StepType.Post, parent.getName() + "/" + c.getName()));
			return VisitResult.Continue;
		}

		@Override
		public VisitResult pre(A a) {
			events.add(new Step<>(StepType.Pre, a.getName()));
			return VisitResult.Continue;
		}

		@Override
		public VisitResult pre(B b) {
			events.add(new Step<>(StepType.Pre, b.getName()));
			return VisitResult.Continue;
		}

		@Override
		public VisitResult pre(INode parent, C c) {
			events.add(new Step<>(StepType.Pre, parent.getName() + "/" + c.getName()));
			return VisitResult.Continue;
		}

	}

	public static interface INode {
		public String getName();
	}

	/**
	 * This is a crude example of a visitor, with pre and post methods for each type. When creating your own visitor, it will be common to have fewer methods,
	 * and for them to be more sensibly named.
	 * 
	 * Your domain specific visitor need not return {@link VisitResult}. Those values can be provided by the {@link IWalkNodeAccessor} instead, if the visitor
	 * does not need to make any decisions about what the visit result should be.
	 */
	public interface IVisitor {
		public VisitResult post(A a);

		public VisitResult post(B b);

		/**
		 * Visit a node of type {@link C}.
		 * 
		 * @param parent The parent of the {@link C} node.
		 * @param c The {@link C} node.
		 * @return
		 */
		public VisitResult post(INode parent, C c);

		public VisitResult pre(A a);

		public VisitResult pre(B b);

		public VisitResult pre(INode parent, C c);
	}

	protected static final Walker<INode, IVisitor> walker;

	static {
		/* This static block constructs a singleton walker which can be used to walk INode hierarchies.
		First we use TypeSwitch1.FunctionBuilder which allows us to create a type-safe, lambda-based accessor function,
		then we use this function to create an instance of Walker.*/
		final FunctionBuilder<INode, IWalkNodeAccessor<INode, IVisitor>> accessorBuilder = new TypeSwitch1.FunctionBuilder<>();
		// Add a handler for objects of type A.  Note that due to our use of generics in TypeSwitch1.FunctionBuilder, then "a" argument to the lambda is typed.
		accessorBuilder.add(A.class, a -> new IWalkNodeAccessor<INode, IVisitor>() {
			@Override
			public Collection<? extends INode> getChildren() {
				return a.getChildren();
			}

			@Override
			public VisitResult post(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.post(a);
			}

			@Override
			public VisitResult pre(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.pre(a);
			}
		});
		accessorBuilder.add(B.class, b -> new IWalkNodeAccessor<INode, IVisitor>() {
			@Override
			public Collection<? extends INode> getChildren() {
				return HCollection.asList(b.getLeft(), b.getRight()).stream().filter(Objects::nonNull).collect(Collectors.toList());
			}

			@Override
			public VisitResult post(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.post(b);
			}

			@Override
			public VisitResult pre(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.pre(b);
			}
		});
		accessorBuilder.add(C.class, c -> new IWalkNodeAccessor<INode, IVisitor>() {
			@Override
			public Collection<? extends INode> getChildren() {
				return HCollection.emptyList();
			}

			@Override
			public VisitResult post(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.post(HCollection.getLast(context.get()), c);
			}

			@Override
			public VisitResult pre(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.pre(HCollection.getLast(context.get()), c);
			}
		});
		// NOTE: We do NOT supply a lambda to create walk node accessors for objects of type D

		// Create the walker.  This walker can be re-used by all of the unit tests.
		walker = new Walker<INode, IVisitor>(accessorBuilder.build());
	}

	/**
	 * Ensure that {@link VisitResult#SkipChildren} works correctly.
	 */
	@Test
	public void skipChildren() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(A a) {
				// We override this method to return a different VisitResult
				super.pre(a);
				return VisitResult.SkipChildren;
			}
		};
		final A n1 = A.builder().name("1").child(new C("2")).child(new C("3")).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that {@link VisitResult#SkipSiblings} works correctly when returned by a post visit.
	 */
	@Test
	public void skipSiblingsPost() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult post(INode parent, C c) {
				// We override this method to return a different VisitResult
				super.post(parent, c);
				return VisitResult.SkipSiblings;
			}
		};
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n1.getName() + "/" + n2.getName()), new Step<>(StepType.Post, n1.getName() + "/" + n2.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that {@link VisitResult#SkipSiblings} works correctly when returned by a pre visit.
	 */
	@Test
	public void skipSiblingsPre() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(INode parent, C c) {
				// We override this method to return a different VisitResult
				super.pre(parent, c);
				return VisitResult.SkipSiblings;
			}
		};
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n1.getName() + "/" + n2.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that {@link VisitResult#Terminate} works correctly.
	 */
	@Test
	public void terminate() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(A a) {
				// We override this method to return a different VisitResult
				super.pre(a);
				return VisitResult.Terminate;
			}
		};
		final A n1 = A.builder().name("1").build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that we can walk a trivial data structure.
	 */
	@Test
	public void walk() {
		final EventVisitor visitor = new EventVisitor();
		final A n1 = A.builder().name("1").build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that we can walk a data structure where some nodes have children.
	 */
	@Test
	public void walkChildren() {
		final EventVisitor visitor = new EventVisitor();
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n1.getName() + "/" + n2.getName()), new Step<>(StepType.Post, n1.getName() + "/" + n2.getName()), new Step<>(StepType.Pre, n1.getName() + "/" + n3.getName()), new Step<>(StepType.Post, n1.getName() + "/" + n3.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	/**
	 * Ensure that attempting to walk a data structure with a node type that doesn't have an {@link IWalkNodeAccessor} fails correctly.
	 */
	@Test(expected = NoWalkNodeAccessorException.class)
	public void walkUnknownType() {
		walker.walk(new D("1"), null /* We don't even need a visitor, since the only node in the structure has no walk node accessor */);
	}
}
