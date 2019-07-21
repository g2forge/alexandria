package com.g2forge.alexandria.analysis;

import java.util.stream.Stream;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.reflect.HReflection;
import com.g2forge.alexandria.java.reflect.IJavaAccessorMethod;
import com.g2forge.alexandria.java.reflect.JavaPrimitive;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class MethodAnalyzer implements IMethodAnalyzer {
	protected static String computePath(org.apache.bcel.classfile.Method method) throws Error {
		if (method.isAbstract()) return new IJavaAccessorMethod() {
			@Override
			public String getName() {
				return method.getName();
			}

			@Override
			public java.lang.reflect.Type[] getParameterTypes() {
				return HBCEL.toJava(method.getArgumentTypes());
			}

			@Override
			public java.lang.reflect.Type getReturnType() {
				return HBCEL.toJava(method.getReturnType());
			}
		}.getFieldName();

		final Code code = method.getCode();
		final Instruction[] instructions = new InstructionList(code.getCode()).getInstructions();
		final int limit = instructions.length - 1;

		// Get/Set chains always start with an aload, and end with a return
		if (!(instructions[0] instanceof ALOAD) || (((ALOAD) instructions[0]).getIndex() != 0)) throw new Error("Cannot parse method to path, first instruction is not a load of this");
		if (!(instructions[limit] instanceof ReturnInstruction)) throw new Error("Cannot parse method to path, because last instruction is not a return");

		// Parse through all the instructions in the middle
		final StringBuilder retVal = new StringBuilder();
		final ConstantPoolGen constantPoolGen = new ConstantPoolGen(method.getConstantPool());
		for (int i = 1; i < limit; i++) {
			// The field we're traversing on this instruction, can be null if the instruction is not a field traversal
			final String field;

			if (instructions[i] instanceof INVOKEVIRTUAL) {
				// Handle getters
				final InvokeInstruction invoke = (InvokeInstruction) instructions[i];
				try {
					field = getField(invoke.getReferenceType(constantPoolGen), invoke.getMethodName(constantPoolGen), invoke.getReturnType(constantPoolGen), invoke.getArgumentTypes(constantPoolGen));
				} catch (ClassNotFoundException e) {
					throw new RuntimeReflectionException(e);
				}
			} else if (instructions[i] instanceof GETFIELD) {
				// Handle an actual field reference
				final GETFIELD get = ((GETFIELD) instructions[i]);
				field = get.getFieldName(constantPoolGen);
			} else if (instructions[i] instanceof INVOKESTATIC) {
				// Allow for auto-boxing of primitive types along the field access path
				final InvokeInstruction invoke = (InvokeInstruction) instructions[i];
				if (!"valueOf".equals(invoke.getMethodName(constantPoolGen))) throw new Error();
				final String name = invoke.getReferenceType(constantPoolGen).toString();
				if (!Stream.of(JavaPrimitive.values()).filter(primitive -> primitive.getBoxed().getName().equals(name)).findAny().isPresent()) throw new Error();
				field = null;
			} else if (instructions[i] instanceof INVOKEINTERFACE) {
				final INVOKEINTERFACE invoke = ((INVOKEINTERFACE) instructions[i]);
				field = new IJavaAccessorMethod() {
					@Override
					public String getName() {
						return invoke.getMethodName(constantPoolGen);
					}

					@Override
					public java.lang.reflect.Type[] getParameterTypes() {
						return HBCEL.toJava(invoke.getArgumentTypes(constantPoolGen));
					}

					@Override
					public java.lang.reflect.Type getReturnType() {
						return HBCEL.toJava(invoke.getReturnType(constantPoolGen));
					}
				}.getFieldName();
				if (field == null) throw new Error("Method was not an accessor");
			} else if (instructions[i] instanceof ALOAD) {
				final int i1 = i + 1;
				if ((i1 != limit - 1) || (((ALOAD) instructions[i]).getIndex() != 1)) throw new Error();
				field = ((PUTFIELD) instructions[i1]).getFieldName(constantPoolGen);
				i++;
			} else {
				throw new Error(String.format("Instruction %d (%s) is of type %s which not recognized!", i, instructions[i], instructions[i].getClass().getSimpleName()));
			}

			if (field != null) {
				if (retVal.length() > 0) retVal.append('.');
				retVal.append(field);
			}
		}
		return retVal.toString();
	}

	protected static String getField(Type target, String name, Type ret, Type[] args) throws ClassNotFoundException {
		final JavaClass clazz = Repository.lookupClass(target.toString());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(name) && method.getSignature().equals(Type.getMethodSignature(ret, args))) {
				final Code code = method.getCode();
				final Instruction[] instructions = new InstructionList(code.getCode()).getInstructions();
				if (instructions.length != 3) throw new Error("Method " + method + " does not have exactly three instruction!");
				if (!(instructions[0] instanceof ALOAD) || (((ALOAD) instructions[0]).getIndex() != 0)) throw new Error();
				if (!(instructions[instructions.length - 1] instanceof ReturnInstruction)) throw new Error();

				final ConstantPoolGen constantPoolGen = new ConstantPoolGen(method.getConstantPool());
				final GETFIELD get = ((GETFIELD) instructions[1]);
				return get.getFieldName(constantPoolGen);
			}
		}
		throw new Error();
	}

	protected static org.apache.bcel.classfile.Method toBCELMethod(final T thunk) {
		final JavaClass clazz;
		try {
			clazz = Repository.lookupClass(thunk.getImplClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeReflectionException(e);
		}

		for (org.apache.bcel.classfile.Method method : clazz.getMethods()) {
			if (method.getName().equals(thunk.getImplMethodName()) && method.getSignature().equals(thunk.getImplMethodSignature())) return method;
		}
		throw new RuntimeReflectionException("Could not find method \"" + thunk.getImplClass() + "." + thunk.getImplMethodName() + "\"");
	}

	protected static java.lang.reflect.Method toJavaMethod(final T thunk) {
		final Class<?> clazz;
		try {
			clazz = thunk.getClass().getClassLoader().loadClass(thunk.getImplClass().replace('/', '.'));
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException(exception);
		}

		for (java.lang.reflect.Method[] methods : new java.lang.reflect.Method[][] { clazz.getMethods(), clazz.getDeclaredMethods() }) {
			for (java.lang.reflect.Method method : methods) {
				if (method.getName().equals(thunk.getImplMethodName())) {
					final String signature = HReflection.toSignature(method);
					if (signature.equals(thunk.getImplMethodSignature())) return method;
				}
			}
		}
		throw new RuntimeReflectionException("Could not find method \"" + thunk.getImplClass() + "." + thunk.getImplMethodName() + "\"");
	}

	@Getter(value = AccessLevel.PROTECTED)
	protected final SerializableLambda lambda;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final T thunk = T.create(getLambda());

	@Getter(lazy = true)
	private final java.lang.reflect.Method method = toJavaMethod(getThunk());

	@Getter(lazy = true)
	private final String path = computePath(getBCEL());

	@Getter(lazy = true)
	private final org.apache.bcel.classfile.Method BCEL = toBCELMethod(getThunk());
}