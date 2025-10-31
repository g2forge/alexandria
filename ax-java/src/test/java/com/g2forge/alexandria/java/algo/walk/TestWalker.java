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
		public VisitResult post(C c) {
			events.add(new Step<>(StepType.Post, c.getName()));
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
		public VisitResult pre(C c) {
			events.add(new Step<>(StepType.Pre, c.getName()));
			return VisitResult.Continue;
		}

	}

	public static interface INode {
		public String getName();
	}

	public interface IVisitor {
		public VisitResult post(A a);

		public VisitResult post(B b);

		public VisitResult post(C c);

		public VisitResult pre(A a);

		public VisitResult pre(B b);

		public VisitResult pre(C c);
	}

	protected static final Walker<INode, IVisitor> walker;

	static {
		final FunctionBuilder<INode, IWalkNodeAccessor<INode, IVisitor>> accessorBuilder = new TypeSwitch1.FunctionBuilder<>();
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
				return visitor.post(c);
			}

			@Override
			public VisitResult pre(ISupplier<? extends List<? extends INode>> context, IVisitor visitor) {
				return visitor.pre(c);
			}
		});
		walker = new Walker<INode, IVisitor>(accessorBuilder.build());
	}

	@Test
	public void skipChildren() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(A a) {
				super.pre(a);
				return VisitResult.SkipChildren;
			}
		};
		final A n1 = A.builder().name("1").child(new C("2")).child(new C("3")).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	@Test
	public void skipSiblingsPost() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult post(C c) {
				super.post(c);
				return VisitResult.SkipSiblings;
			}
		};
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n2.getName()), new Step<>(StepType.Post, n2.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	@Test
	public void skipSiblingsPre() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(C c) {
				super.pre(c);
				return VisitResult.SkipSiblings;
			}
		};
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n2.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	@Test
	public void terminate() {
		final EventVisitor visitor = new EventVisitor() {
			@Override
			public VisitResult pre(A a) {
				super.pre(a);
				return VisitResult.Terminate;
			}
		};
		final A n1 = A.builder().name("1").build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName())), visitor.getEvents());
	}

	@Test
	public void walk() {
		final EventVisitor visitor = new EventVisitor();
		final A n1 = A.builder().name("1").build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}

	@Test
	public void walkChildren() {
		final EventVisitor visitor = new EventVisitor();
		final C n2 = new C("2"), n3 = new C("3");
		final A n1 = A.builder().name("1").child(n2).child(n3).build();
		walker.walk(n1, visitor);
		Assert.assertEquals(HCollection.asList(new Step<>(StepType.Pre, n1.getName()), new Step<>(StepType.Pre, n2.getName()), new Step<>(StepType.Post, n2.getName()), new Step<>(StepType.Pre, n3.getName()), new Step<>(StepType.Post, n3.getName()), new Step<>(StepType.Post, n1.getName())), visitor.getEvents());
	}
}
