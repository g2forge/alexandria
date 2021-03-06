package com.g2forge.alexandria.java.type.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

public class TestTypeSwitch1 {
	public interface A {}

	public interface B {}

	public interface C extends A {}

	public interface D extends B, C {}

	public interface E extends B {}

	public interface F extends E {}

	public interface G extends D {}

	@Data
	@Builder
	@AllArgsConstructor
	protected static class TestNode {
		public static TestNode from(TypeSwitch1.Node<?> node) {
			final Set<TestNode> children = node.getChildren().isEmpty() ? null : node.getChildren().stream().map(TestNode::from).collect(Collectors.toSet());
			return new TestNode(node.getFunction() == null ? null : node.getFunction().getInputType().getSimpleName(), children);
		}

		protected final String name;

		@Singular
		protected final Set<TestNode> children;

		public TestNode(String name) {
			this(name, null);
		}

		@Override
		public String toString() {
			if (getChildren() == null) return getName();
			return getName() + "{" + getChildren().stream().map(Object::toString).collect(Collectors.joining(", ")) + "}";
		}
	}

	protected static List<? extends TypedFunction1<?, ?>> getTypedFunctions(List<Class<?>> order) {
		final List<TypedFunction1<?, ?>> retVal = new ArrayList<>(order.size());
		for (Class<?> type : order) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final TypedFunction1<?, ?> tf = new TypedFunction1<>((Class) type, null);
			retVal.add(tf);
		}
		return retVal;
	}

	public static <T> List<List<T>> permute(List<T> list) {
		return permute(HCollection.emptyList(), list);
	}

	protected static <T> List<List<T>> permute(List<T> prefix, List<T> remaining) {
		final int size = remaining.size();
		if (size == 0) return Arrays.asList(prefix);
		final List<List<T>> retVal = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			final List<T> childPrefix = HCollection.concatenate(prefix, HCollection.asList(remaining.get(i)));
			final List<T> childRemaining = HCollection.concatenate(remaining.subList(0, i), remaining.subList(i + 1, size));
			retVal.addAll(permute(childPrefix, childRemaining));
		}
		return retVal;
	}

	@Test
	public void basic() {
		final String context = "a";

		final TypeSwitch1.FunctionBuilder<Object, Object> builder = new TypeSwitch1.FunctionBuilder<>();
		builder.add(String.class, t -> context + t);
		builder.add(Integer.class, t -> context + Integer.toString(t + 1));
		final IFunction1<Object, Object> typeSwitch = builder.build();

		Assert.assertEquals("ab", typeSwitch.apply("b"));
		Assert.assertEquals("a2", typeSwitch.apply(1));
	}

	/**
	 * Test what happens when there are 0 valid implementations.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error0() {
		final TypeSwitch1.FunctionBuilder<Object, Object> builder = new TypeSwitch1.FunctionBuilder<>();
		builder.add(A.class, t -> null);
		final IFunction1<Object, Object> typeSwitch = builder.build();

		typeSwitch.apply(new B() {});
	}

	/**
	 * Test what happens when there are 2 valid implementations.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void error2() {
		final TypeSwitch1.FunctionBuilder<Object, Object> builder = new TypeSwitch1.FunctionBuilder<>();
		builder.add(A.class, t -> null);
		builder.add(B.class, t -> null);
		final IFunction1<Object, Object> typeSwitch = builder.build();

		typeSwitch.apply(new D() {});
	}

	/**
	 * Test what happens when we specify two identical functions
	 */
	@Test
	@Note(type = NoteType.TODO, value = "This should throw an error!")
	public void errorDuplicate() {
		final TypeSwitch1.FunctionBuilder<Object, Object> builder = new TypeSwitch1.FunctionBuilder<>();
		builder.add(A.class, t -> null);
		builder.add(A.class, t -> null);
		builder.build();
	}

	@Test
	public void shadow() {
		final TypeSwitch1.FunctionBuilder<Object, Object> builder = new TypeSwitch1.FunctionBuilder<>();
		builder.add(String.class, t -> t);
		builder.add(Object.class, t -> null);
		final IFunction1<Object, Object> typeSwitch = builder.build();

		Assert.assertEquals("b", typeSwitch.apply("b"));
	}

	@Test
	public void treeA() {
		final List<TypedFunction1<?, Object>> functions = new ArrayList<>();
		functions.add(new TypedFunction1<>(A.class, null));
		final TestNode actual = TestNode.from(new TypeSwitch1<>(null, functions).getRoot());
		Assert.assertEquals(new TestNode("A", null), actual);
	}

	@Test
	public void treeAB() {
		final List<TypedFunction1<?, Object>> functions = new ArrayList<>();
		functions.add(new TypedFunction1<>(A.class, null));
		functions.add(new TypedFunction1<>(B.class, null));
		final TestNode actual = TestNode.from(new TypeSwitch1<>(null, functions).getRoot());
		Assert.assertEquals(TestNode.builder().child(new TestNode("A")).child(new TestNode("B")).build(), actual);
	}

	@Test
	public void treeABC() {
		final List<TypedFunction1<?, Object>> functions = new ArrayList<>();
		functions.add(new TypedFunction1<>(A.class, null));
		functions.add(new TypedFunction1<>(B.class, null));
		functions.add(new TypedFunction1<>(C.class, null));
		final TestNode actual = TestNode.from(new TypeSwitch1<>(null, functions).getRoot());
		Assert.assertEquals(TestNode.builder().child(TestNode.builder().name("A").child(new TestNode("C")).build()).child(new TestNode("B")).build(), actual);
	}

	@Test
	public void treeABDG() {
		final List<Class<? extends Object>> types = HCollection.asList(A.class, B.class, D.class, G.class);

		final TestNode d = TestNode.builder().name("D").child(new TestNode("G")).build();
		final TestNode expected = TestNode.builder().child(TestNode.builder().name("A").child(d).build()).child(TestNode.builder().name("B").child(d).build()).build();

		for (List<Class<? extends Object>> order : permute(types)) {
			final TestNode actual = TestNode.from(new TypeSwitch1<>(null, getTypedFunctions(order)).getRoot());
			Assert.assertEquals(order.stream().map(Class::getSimpleName).collect(Collectors.joining()), expected, actual);
		}
	}

	@Test
	public void treeEverything() {
		final List<Class<? extends Object>> types = HCollection.asList(A.class, B.class, C.class, D.class, E.class, F.class, G.class);

		final TestNode d = TestNode.builder().name("D").child(new TestNode("G")).build();
		final TestNode b = TestNode.builder().name("B").child(d).child(TestNode.builder().name("E").child(new TestNode("F")).build()).build();
		final TestNode expected = TestNode.builder().child(TestNode.builder().name("A").child(TestNode.builder().name("C").child(d).build()).build()).child(b).build();

		for (List<Class<?>> order : permute(types)) {
			final TestNode actual = TestNode.from(new TypeSwitch1<>(null, getTypedFunctions(order)).getRoot());
			Assert.assertEquals(order.stream().map(Class::getSimpleName).collect(Collectors.joining()), expected, actual);
		}
	}
}
