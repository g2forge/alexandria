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
import org.apache.bcel.generic.ReturnInstruction;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.marker.Helpers;
import com.g2forge.alexandria.java.reflect.HReflection;
import com.g2forge.alexandria.java.reflect.IJavaAccessorMethod;
import com.g2forge.alexandria.java.reflect.JavaPrimitive;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAnalysis {
	public static java.lang.reflect.Method getMethod(final SerializableFunction<?, ?> lambda) {
		return getJavaMethod(T.create(lambda));
	}

	protected static java.lang.reflect.Method getJavaMethod(final T thunk) {
		final Class<?> clazz;
		try {
			clazz = thunk.getClass().getClassLoader().loadClass(thunk.getImplClass().replace('/', '.'));
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException(exception);
		}

		for (java.lang.reflect.Method method : clazz.getMethods()) {
			if (method.getName().equals(thunk.getImplMethodName())) {
				final String signature = HReflection.toSignature(method);
				if (signature.equals(thunk.getImplMethodSignature())) return method;
			}
		}
		throw new RuntimeReflectionException("Could not find method \"" + thunk.getImplClass() + "." + thunk.getImplMethodName() + "\"");
	}

	protected static Method getBCELMethod(final T thunk) {
		final JavaClass clazz;
		try {
			clazz = Repository.lookupClass(thunk.getImplClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeReflectionException(e);
		}

		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(thunk.getImplMethodName()) && method.getSignature().equals(thunk.getImplMethodSignature())) return method;
		}
		throw new RuntimeReflectionException("Could not find method \"" + thunk.getImplClass() + "." + thunk.getImplMethodName() + "\"");
	}

	public static String getPath(final SerializableSupplier<?> lambda) {
		return extractPath(getBCELMethod(T.create(lambda)));
	}

	public static java.lang.reflect.Method getMethod(final SerializableSupplier<?> lambda) {
		return getJavaMethod(T.create(lambda));
	}

	public static String getPath(final SerializableFunction<?, ?> lambda) {
		return extractPath(getBCELMethod(T.create(lambda)));
	}

	protected static String extractPath(Method method) throws Error {
		if (method.isAbstract()) return new IJavaAccessorMethod() {
			@Override
			public String getName() {
				return method.getName();
			}

			@Override
			public java.lang.reflect.Type[] getParameterTypes() {
				return HBCEL.getTypes(method.getArgumentTypes());
			}

			@Override
			public java.lang.reflect.Type getReturnType() {
				return HBCEL.getType(method.getReturnType());
			}
		}.getFieldName();

		final Code code = method.getCode();
		final Instruction[] instructions = new InstructionList(code.getCode()).getInstructions();
		if (!(instructions[0] instanceof ALOAD) || (((ALOAD) instructions[0]).getIndex() != 0)) throw new Error();
		if (!(instructions[instructions.length - 1] instanceof ReturnInstruction)) throw new Error();

		final StringBuilder retVal = new StringBuilder();
		final ConstantPoolGen constantPoolGen = new ConstantPoolGen(method.getConstantPool());
		for (int i = 1; i < instructions.length - 1; i++) {
			final String field;

			if (instructions[i] instanceof INVOKEVIRTUAL) {
				final InvokeInstruction invoke = (InvokeInstruction) instructions[i];
				try {
					field = T.getField(invoke.getReferenceType(constantPoolGen), invoke.getMethodName(constantPoolGen), invoke.getReturnType(constantPoolGen), invoke.getArgumentTypes(constantPoolGen));
				} catch (ClassNotFoundException e) {
					throw new RuntimeReflectionException(e);
				}
			} else if (instructions[i] instanceof GETFIELD) {
				final GETFIELD get = ((GETFIELD) instructions[i]);
				field = get.getFieldName(constantPoolGen);
			} else if (instructions[i] instanceof INVOKESTATIC) {
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
						return HBCEL.getTypes(invoke.getArgumentTypes(constantPoolGen));
					}

					@Override
					public java.lang.reflect.Type getReturnType() {
						return HBCEL.getType(invoke.getReturnType(constantPoolGen));
					}
				}.getFieldName();
				if (field == null) throw new Error("Method was not an accessor");
			} else throw new Error(String.format("Instruction %d (%s) is of type %s which not recognized!", i, instructions[i], instructions[i].getClass().getSimpleName()));

			if (field != null) {
				if (retVal.length() > 0) retVal.append('.');
				retVal.append(field);
			}
		}
		return retVal.toString();
	}
}
